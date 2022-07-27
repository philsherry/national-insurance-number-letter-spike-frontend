/// <reference types="cypress" />
// https://on.cypress.io/custom-commands
import './govuk-cypress/index';

/**
 * @link https://docs.cypress.io/api/cypress-api/screenshot-api
 */
Cypress.Screenshot.defaults({
  capture: 'fullPage',
  disableTimersAndAnimations: false,
  overwrite: true,
  screenshotOnRunFailure: false
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
    name: 'compareStrings'
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
 */
Cypress.Commands.add('checkNino', (parentEl, ninoEl, checkNino) => {
  cy.get(parentEl).then(($nino) => {
    let ninoText = $nino.find(ninoEl).text().trim();
    expect(ninoText).to.equal(checkNino);
    expect(ninoText).to.match(/^\s*[a-zA-Z]{2}(?:\s*\d\s*){6}[a-zA-Z]?\s*$/);
  });
});
