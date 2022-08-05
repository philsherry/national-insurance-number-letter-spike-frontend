/// <reference types="cypress" />
// https://on.cypress.io/custom-commands
import user from '../fixtures/user.json';
import './govuk-cypress/index';
import { v4 as uuidv4 } from 'uuid';

/**
 * @link https://docs.cypress.io/api/cypress-api/screenshot-api
 */
Cypress.Screenshot.defaults({
  capture: 'fullPage',
  disableTimersAndAnimations: false,
  overwrite: true,
  screenshotOnRunFailure: false,
});

/**
 * @link https://github.com/cypress-io/cypress/issues/877
 * @use cy.isNotInViewport('[data-cy="some-invisible-element"]')
 * @use cy.isInViewport('[data-cy="some-visible-element"]')
 */
Cypress.Commands.add('isNotInViewport', (element) => {
  cy.get(element).then(($el) => {
    const bottom = Cypress.$(cy.state('window')).height();
    const rect = $el[0].getBoundingClientRect();

    expect(rect.top).to.be.greaterThan(bottom);
    expect(rect.bottom).to.be.greaterThan(bottom);
    expect(rect.top).to.be.greaterThan(bottom);
    expect(rect.bottom).to.be.greaterThan(bottom);
  });
});

Cypress.Commands.add('isInViewport', (element) => {
  cy.get(element).then(($el) => {
    const bottom = Cypress.$(cy.state('window')).height();
    const rect = $el[0].getBoundingClientRect();

    expect(rect.top).not.to.be.greaterThan(bottom);
    expect(rect.bottom).not.to.be.greaterThan(bottom);
    expect(rect.top).not.to.be.greaterThan(bottom);
    expect(rect.bottom).not.to.be.greaterThan(bottom);
  });
});

/**
 * @link https://github.com/cypress-io/cypress/issues/2186#issuecomment-606796041
 * @link https://gist.github.com/NicholasBoll/e584991b36986a85acf0e95101752bc0
 */
const compareColor = (color, property) => (targetElement) => {
  const tempElement = document.createElement('div');
  tempElement.style.color = color;
  tempElement.style.display = 'none'; // make sure it doesn't actually render
  document.body.appendChild(tempElement); // append so that `getComputedStyle` actually works

  const tempColor = getComputedStyle(tempElement).color;
  const targetColor = getComputedStyle(targetElement[0])[property];

  document.body.removeChild(tempElement); // remove it because we're done with it

  expect(tempColor).to.equal(targetColor);
};

Cypress.Commands.overwrite(
  'should',
  (originalFn, subject, expectation, ...args) => {
    const customMatchers = {
      'have.backgroundColor': compareColor(args[0], 'backgroundColor'),
      'have.color': compareColor(args[0], 'color'),
    };

    // See if the expectation is a string and if it is a member of Jest's expect
    if (typeof expectation === 'string' && customMatchers[expectation]) {
      return originalFn(subject, customMatchers[expectation]);
    }
    return originalFn(subject, expectation, ...args);
  }
);

Cypress.Commands.add('authLoginHeadless', (auth) => {
  Cypress.log({
    name: 'authLoginHeadless',
    message: `${auth.url}`,
  });
  console.log({ auth });

  return cy.request({
    body: {
      authorityId: `${Date.now()}`,
      affinityGroup: `${auth.body.affinityGroup}`,
      credentialStrength: `${auth.body.credentialStrength}`,
      confidenceLevel: `${auth.body.confidenceLevel}`,
      redirectionUrl: `${auth.body.redirectionUrl}`,
      nino: `${auth.body.nino}`,
    },
    form: true,
    method: 'POST',
    url: `${auth.url}`,
  });
});

Cypress.Commands.add('authLoginBrowser', (auth) => {
  Cypress.log({
    name: 'Auth Object',
    message: `${auth}`,
  });
  cy.visit('http://localhost:9949/auth-login-stub/gg-sign-in');
  cy.get('#authorityId').type(auth.authorityId); // CredID
  cy.get('#redirectionUrl').type(auth.redirectionUrl);
  cy.get('#credentialStrength').select(auth.credentialStrength);
  cy.get('#confidenceLevel').select(auth.confidenceLevel);
  cy.get('#affinityGroupSelect').select(auth.affinityGroup);
  cy.get('#nino').type(auth.nino);
  cy.get('#submit').click();
});

/**
 * @description Compare the content of two elements. Normalises whitespace and ignores case to match only the content—not the style.
 * @use cy.compareStrings('element1', 'element2');
 */
Cypress.Commands.add('compareStrings', (element1, element2) => {
  Cypress.log({
    displayName: 'COMPARE',
    message: `${element1} || ${element2}`,
    name: 'compareStrings',
  });
  /**
   * Text from the first element.
   * @type {string}
   */
  let text;

  /**
   * Normalizes passed text,
   * useful before comparing text with spaces and different capitalization.
   * @param {string} s Text to normalize
   */
  const normalizeText = (s) => s.replace(/\s/g, '').toLowerCase();

  cy.get(element1).then(($first) => {
    // save text from the first element
    text = normalizeText($first.text());
  });

  cy.get(element2).should(($div) => {
    // we can massage text before comparing
    const secondString = normalizeText($div.text());

    expect(secondString, 'second string').to.equal(text);
  });
});

/**
 * Check a NINO
 **/
Cypress.Commands.add('checkNino', (parentEl, ninoEl, checkNino) => {
  cy.get(parentEl).then(($nino) => {
    let ninoText = $nino.find(ninoEl).text().trim();
    expect(ninoText).to.equal(checkNino);
    expect(ninoText).to.match(/^\s*[a-zA-Z]{2}(?:\s*\d\s*){6}[a-zA-Z]?\s*$/);
  });
});

/**
 * @description Cockamamie login to force a session cookie for the postal NINO service. We need to hit `setCookiePage` to get the cookie. Then we check a page after that in the journey (`validatePage`) to make sure the cookie is set.
 * @see https://github.com/cypress-io/cypress-example-recipes/blob/master/examples/logging-in__csrf-tokens/cypress/e2e/logging-in-csrf-tokens-spec.cy.js
 */

Cypress.Commands.add('loginByCSRF', (csrfToken) => {
  Cypress.log({
    displayName: 'loginByCSRF',
    message: 'Login by CSRF',
    name: 'loginByCSRF',
  });
  const port = 11300;
  const serviceID = 'fill-online/get-your-national-insurance-number-by-post';
  const setCookiePage = 'what-is-your-name';
  const checkValidateSlug = 'what-is-your-previous-name';
  const cookiePage = `http://localhost:${port}/${serviceID}/${setCookiePage}`;
  const validatePage = `http://localhost:${port}/${serviceID}/${checkValidateSlug}`;
  const firstName = user.username.firstName;
  const lastName = user.username.lastName;

  cy.request(
    {
      method: 'POST',
      url: cookiePage,
      failOnStatusCode: false, // don’t fail so we can make assertions
      form: true, // we are submitting a regular form body
      body: {
        csrfToken: csrfToken, // insert this as part of form body
        firstName: firstName,
        lastName: lastName,
        validatePage: validatePage
      },
    },
    {
      validate() {
        cy.visit(`${validatePage}`, { failOnStatusCode: false });
      },
    }
  );

  // Logging for development purposes; remove once tests are finalised.
  console.log('loginByCSRF', {
    csrfToken,
    firstName,
    lastName,
    validatePage,
  });
});

// userUuid, pageSlug, firstName, lastName
Cypress.Commands.add('loginForCookie', (userUuid, pageSlug) => {
  const port = 11300;
  const serviceID = 'fill-online/get-your-national-insurance-number-by-post';
  const setCookiePage = 'what-is-your-name';
  const cookiePage = `http://localhost:${port}/${serviceID}/${setCookiePage}`;
  const firstName = user.username.firstName;
  const lastName = user.username.lastName;

  Cypress.log({
    displayName: 'loginForCookie',
    message: 'Login for session cookie',
    name: 'loginForCookie',
  });

  console.log({ userUuid, pageSlug, firstName, lastName });

  cy.session(
    [userUuid, pageSlug, firstName, lastName],
    () => {
      console.log('LOGGING_INSIDE_THE_SESSION_COMMAND', {
        userUuid,
        firstName,
        lastName,
        pageSlug,
      });
      cy.request({
        method: 'POST',
        url: cookiePage,
        body: {
          userUuid: userUuid,
          firstName: firstName,
          lastName: lastName,
          pageSlug: pageSlug,
        },
      }).then((response) => {
        expect(response.status).to.eq(200);
        expect(response).to.have.property('headers');
        expect(response).to.have.property('duration');
      });
    },
    {
      validate() {
        cy.visit(`${pageSlug}`);
      },
    }
  );
});
