/* eslint-disable no-undef, no-unused-vars, cypress/no-unnecessary-waiting, sonarjs/no-duplicate-string, sonarjs/no-identical-functions */
let pageLoop = 1;
const port = 11300;
const serviceID = 'fill-online/get-your-national-insurance-number-by-post';
const pageSlug = `${serviceID}/what-is-your-date-of-birth`;
const nextSlug = `${serviceID}/what-is-your-gender`;
const thisPage = `http://localhost:${port}/${pageSlug}`;
const nextPage = `http://localhost:${port}/${nextSlug}`;
const pageTitleEN = 'What is your date of birth?';
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
const route = 'what-is-your-date-of-birth';
const viewport = 'desktop';
let thing = '';
let state = 'default';

describe(`Page:: ${pageTitleEN}`, () => {
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
      cy.get('.hmrc-page-heading').as('what_is_your_dob__heading');
      cy.get('#value').as('what_is_your_dob__group');
      cy.get('#value\\.day').as('what_is_your_dob__value_day');
      cy.get('#value\\.month').as('what_is_your_dob__value_month');
      cy.get('#value\\.year').as('what_is_your_dob__value_year');
      cy.get('[data-spec="what_is_your_dob__continue"]').as(
        'what_is_your_dob__continue'
      );
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
      cy.screenshot(`${service}--${route}--${viewport}--fullpage--${state}`, {
        capture: 'fullPage',
      });
      cy.get('h1').should('exist');
      cy.screenshot(`${service}--${route}--${viewport}--${state}`, {
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
    it('checks the page heading', () => {});

    it('checks form elements exist', () => {
      cy.get('@what_is_your_dob__value_day').should('exist');
      cy.get('@what_is_your_dob__value_month').should('exist');
      cy.get('@what_is_your_dob__value_year').should('exist');
      cy.get('@what_is_your_dob__continue').should('exist');
    });

    it('checks error states are handled', () => {
      cy.get('@what_is_your_dob__continue').click();
      cy.get('.govuk-error-summary').checkGovukErrorSummary();
      cy.get('@what_is_your_dob__value_day').checkGovukErrorMessage();
      cy.get('@what_is_your_dob__value_month').checkGovukErrorMessage();
      cy.get('@what_is_your_dob__value_year').checkGovukErrorMessage();
      // cy.checkPageTitle('hasError');
    });
  });

  describe('Go to the next page', () => {
    it('submits the form', () => {
      // ADD_SOME_NEXT_PAGE_CHECKS
      cy.get('@what_is_your_dob__value_day').type(user.dateOfBirth.day);
      cy.get('@what_is_your_dob__value_month').type(user.dateOfBirth.month);
      cy.get('@what_is_your_dob__value_year').type(user.dateOfBirth.year);
      cy.get('@what_is_your_dob__continue').click();
      cy.location('pathname').should(($url) => {
        expect($url).to.include(nextSlug);
      });
      // NEXT_PAGE: what-is-your-current-address-uk
    });
  });
});
