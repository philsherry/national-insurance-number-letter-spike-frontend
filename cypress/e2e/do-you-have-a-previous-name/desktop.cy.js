/* eslint-disable no-undef, no-unused-vars, cypress/no-unnecessary-waiting, sonarjs/no-duplicate-string, sonarjs/no-identical-functions */
let pageLoop = 1;
const port = 11300;
const serviceID = 'fill-online/get-your-national-insurance-number-by-post';
const pageSlug = `${serviceID}/do-you-have-a-previous-name`;
const nextSlug = `${serviceID}/${pageLoop}/what-is-your-previous-name`;
const thisPage = `http://localhost:${port}/${pageSlug}`;
const nextPage = `http://localhost:${port}/${nextSlug}`;
const pageTitleEN = 'Have you had any other names?';
const pageTitleCY = '';
const env = require('../../fixtures/env.json');
const user = require('../../fixtures/user.json');
const { terminalLog } = require('../../plugins/hmrc');
const { v4: uuidv4 } = require('uuid');
const userUuid = uuidv4();
const firstName = user.username.firstName;
const lastName = user.username.lastName;

/**
 * @screenshotFormat png
 * @screenshotFileName `${service}--${route}--${viewport}--${thing}--${state}`
 **/
const service = 'national-insurance-number-letter-spike-frontend';
const route = 'do-you-have-a-previous-name';
const viewport = 'desktop';
let thing = '';
let state = 'default';

describe(`Page:: ${pageTitleEN}`, () => {
  /**
   * Load the initial entry page to grab the cookie for e2e tests.
   */
  before(() => {
    cy.viewport(env.desktop.viewport[0]);
    cy.loginForCookie(userUuid, pageSlug, firstName, lastName);
    cy.visit(pageSlug);
  });

  beforeEach(() => {
    cy.viewport(env.desktop.viewport[0]);
    cy.loginForCookie(userUuid, pageSlug, firstName, lastName);
    cy.visit(pageSlug);
    cy.munchCookies().then(() => {
      cy.getCookie('mdtp').should('exist');
      cy.getCookie('mdtpdi').should('exist');
      cy.getCookie('userConsent').should('exist');
    });

    describe('Set up aliases', () => {
      Cypress.log({
        displayName: 'ALIASES',
        message: 'Set up aliases',
        name: 'setUpAliases',
      });

      // ADD_SOME_ALIASES
      // do_you_have_a_previous_name__heading_with_caption
      // do_you_have_a_previous_name__radio__yes
      // do_you_have_a_previous_name__radio__no
      // #value
      // #value-no
      // do_you_have_a_previous_name__continue
      cy.get('[data-spec="do_you_have_a_previous_name__radio_group"]').as(
        'do_you_have_a_previous_name__radio_group'
      );
      cy.get('#value').as('do_you_have_a_previous_name__radio__yes');
      cy.get('#value-no').as('do_you_have_a_previous_name__radio__no');
      cy.get('[data-spec="do_you_have_a_previous_name__continue"]').as(
        'do_you_have_a_previous_name__continue'
      );
    });
  });

  describe('check we have a session', () => {
    it('403 status without a valid CSRF token', function () {
      // first show that by not providing a valid CSRF token
      // that we will get a 403 status code
      cy.loginByCSRF('invalid-token').its('status').should('eq', 403);
    });

    it('parse CSRF token from HTML', function () {
      // if we cannot change our server code to make it easier
      // to parse out the CSRF token, we can simply use cy.request
      // to fetch the login page, and then parse the HTML contents
      // to find the CSRF token embedded in the page
      cy.request(pageSlug)
        .its('body')
        .then((body) => {
          // we can use Cypress.$ to parse the body
          // enabling us to query into it easily
          const $html = Cypress.$(body);
          const csrf = $html.find('input[name="csrfToken"]').val();

          cy.loginByCSRF(csrf).then((resp) => {
            expect(resp.status).to.eq(200);
            expect(resp.body).to.include(pageTitleEN);
          });
        });
    });
  });

  describe('General page checks', () => {
    it('runs axe against the page', () => {
      cy.visit(pageSlug);
      cy.injectAxe();
      cy.checkA11y(null, null, null, { skipFailures: true });
    });

    it('takes screenshots', () => {
      cy.get('.govuk-header__logo').should('exist');
      cy.screenshot(
        `${service}--${route}--${viewport}--fullpage--${thing}--${state}`,
        {
          capture: 'fullPage',
        }
      );
      cy.get('h1').should('exist');
      cy.screenshot(`${service}--${route}--${viewport}--${thing}--${state}`, {
        capture: 'viewport',
      });
    });

    it('checks we have the correct focus colours', () => {
      thing = 'skip-link';
      state = 'visible';
      cy.get('.govuk-skip-link').as('skipLink');
      cy.get('@skipLink').checkFocusColours();
      cy.get('@skipLink').should('be.visible');
      cy.screenshot(`${service}--${route}--${viewport}--${thing}--${state}`, {
        capture: 'viewport',
      });
    });
  });

  describe('Page content checks', () => {
    //   it('checks the page heading pattern', () => {
    //     // page heading
    //     cy.get('[data-component="hmrc_page_heading"]').as('hmrc_page_heading');
    //     cy.get('[data-component="hmrc_page_heading__h1"]').as(
    //       'hmrc_page_heading__h1'
    //     );
    //     cy.get('[data-component="hmrc_page_heading__p"]').as(
    //       'hmrc_page_heading__p'
    //     );
    //     cy.get('[data-component="hmrc_page_heading__p"] [data-aria-hidden]').as(
    //       'hmrc_page_heading__aria_hidden'
    //     );
    //     cy.get(
    //       '[data-component="hmrc_page_heading__p"] [data-visually-hidden]'
    //     ).as('hmrc_page_heading__visually_hidden');
    //     it('checks the page heading matches the page title', () => {
    //       cy.checkPageTitle();
    //     });

    //     it('checks the visual content and hidden context', () => {
    //       cy.compareStrings(
    //         '@hmrc_page_heading__aria_hidden',
    //         '@hmrc_page_heading__visually_hidden'
    //       );
    //     });
    //   });

    it('checks form elements exist', () => {
      cy.get('@do_you_have_a_previous_name__radio__yes').should('exist');
      cy.get('@do_you_have_a_previous_name__radio__no').should('exist');
      cy.get('@do_you_have_a_previous_name__continue').should('exist');
    });

    it('checks error states are handled', () => {
      cy.get('@do_you_have_a_previous_name__continue').click();
      cy.get('.govuk-error-summary').checkGovukErrorSummary();
      cy.get(
        '@do_you_have_a_previous_name__radio__yes'
      ).checkGovukErrorMessage();
      // cy.checkPageTitle('hasError');
    });
  });

  describe('Go all the wrong ways', () => {
    it('clicks through to the wrong page, then back again', () => {
      thing = 'wrong-way';
      state = 'no';
      cy.get('@do_you_have_a_previous_name__radio__no').click();
      cy.get('@do_you_have_a_previous_name__continue').click();
      cy.screenshot(`${service}--${route}--${viewport}--${thing}--${state}`, {
        capture: 'viewport',
      });
      cy.location('pathname').should('include', 'what-is-your-date-of-birth');
      cy.go(-1);
      cy.location('pathname').should('include', pageSlug);
    });
  });

  describe('Go to the next page', () => {
    it('submits the form', () => {
      cy.get('@do_you_have_a_previous_name__radio__yes').click();
      cy.get('@do_you_have_a_previous_name__continue').click();
      cy.location('pathname').should(($url) => {
        expect($url).to.include(nextSlug);
      });
    });
  });
});
