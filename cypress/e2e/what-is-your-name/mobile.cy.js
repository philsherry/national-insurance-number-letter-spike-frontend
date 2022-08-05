/* eslint-disable no-undef, no-unused-vars, cypress/no-unnecessary-waiting, sonarjs/no-duplicate-string, sonarjs/no-identical-functions */
const page =
  'fill-online/get-your-national-insurance-number-by-post/what-is-your-name';
const pageTitleEN = 'What is your name?';
const pageTitleCY = '';
const env = require('../../fixtures/env.json');
const user = require('../../fixtures/user.json');
const { terminalLog } = require('../../plugins/hmrc');

/**
 * @screenshotFormat png
 * @screenshotFileName `${service}--${route}--${viewport}--${thing}--${state}`
 **/
const service = 'national-insurance-number-letter-spike-frontend';
const route = 'what-is-your-name';
const viewport = 'mobile';
let thing = '';
let state = 'default';

describe(`Page:: ${pageTitleEN}`, () => {
  beforeEach(() => {
    cy.viewport(env.mobile.viewport[0]);
    cy.visit(page);
    cy.injectAxe();
    cy.munchCookies();

    describe('Set up aliases', () => {
      Cypress.log({
        displayName: 'ALIASES',
        message: 'Set up aliases',
        name: 'setUpAliases',
      });

      cy.get('[data-spec="what_is_your_name__heading_with_caption"]').as(
        'what_is_your_name__heading_with_caption'
      );
      cy.get('[data-spec="what_is_your_name__title"]').as(
        'what_is_your_name__title'
      );
      cy.get('[data-spec="what_is_your_name__firstname"]').as(
        'what_is_your_name__firstname'
      );
      cy.get('[data-spec="what_is_your_name__middlenames"]').as(
        'what_is_your_name__middlenames'
      );
      cy.get('[data-spec="what_is_your_name__lastname"]').as(
        'what_is_your_name__lastname'
      );
      cy.get('[data-spec="what_is_your_name__continue"]').as(
        'what_is_your_name__continue'
      );
    });
  });

  describe('General page checks', () => {
    it('runs axe against the page', () => {
      cy.visit(page);
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
    it('checks the page heading pattern', () => {
      // page heading
      cy.get('[data-component="hmrc_page_heading"]').as('hmrc_page_heading');
      cy.get('[data-component="hmrc_page_heading__h1"]').as(
        'hmrc_page_heading__h1'
      );
      cy.get('[data-component="hmrc_page_heading__p"]').as(
        'hmrc_page_heading__p'
      );
      cy.get('[data-component="hmrc_page_heading__p"] [data-aria-hidden]').as(
        'hmrc_page_heading__aria_hidden'
      );
      cy.get(
        '[data-component="hmrc_page_heading__p"] [data-visually-hidden]'
      ).as('hmrc_page_heading__visually_hidden');
      it('checks the page heading matches the page title', () => {
        cy.checkPageTitle();
      });

      it('checks the visual content and hidden context', () => {
        cy.compareStrings(
          '@hmrc_page_heading__aria_hidden',
          '@hmrc_page_heading__visually_hidden'
        );
      });
    });

    it('checks form elements exist', () => {
      cy.get('@what_is_your_name__title').should('exist');
      cy.get('@what_is_your_name__firstname').should('exist');
      cy.get('@what_is_your_name__middlenames').should('exist');
      cy.get('@what_is_your_name__lastname').should('exist');
      cy.get('@what_is_your_name__continue').should('exist');
    });

    it('checks error states are handled', () => {
      cy.get('@what_is_your_name__continue').click();
      cy.get('.govuk-error-summary').as('errorSummary');
      cy.get('@errorSummary').checkGovukErrorSummary();
      cy.get('@what_is_your_name__firstname').checkGovukErrorMessage();
      cy.get('@what_is_your_name__lastname').checkGovukErrorMessage();
      cy.checkPageTitle('hasError');
    });
  });

  describe('Go all the wrong ways', () => {});

  describe('Go to the next page', () => {
    it('submits the form', () => {
      cy.get('@what_is_your_name__title').type('Sir');
      cy.get('@what_is_your_name__firstname').type('David');
      cy.get('@what_is_your_name__middlenames').type('Saint');
      cy.get('@what_is_your_name__lastname').type('Hubbins');
      cy.get('@what_is_your_name__continue').click();
    });
  });
});
