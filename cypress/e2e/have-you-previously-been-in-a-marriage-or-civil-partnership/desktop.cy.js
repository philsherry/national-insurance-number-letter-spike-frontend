/* eslint-disable no-undef, no-unused-vars, cypress/no-unnecessary-waiting, sonarjs/no-duplicate-string, sonarjs/no-identical-functions */
const page = 'fill-online/get-your-national-insurance-number-by-post/have-you-previously-been-in-a-marriage-or-civil-partnership';
const pageTitleEN =
  'Have you previously been in a marriage or civil partnership?';
const pageTitleCY = '';
const env = require('../../fixtures/env.json');
const user = require('../../fixtures/user.json');
const { terminalLog } = require('../../plugins/hmrc');

/**
 * @screenshotFormat png
 * @screenshotFileName `${service}--${route}--${viewport}--${thing}--${state}`
 **/
const service = 'national-insurance-number-letter-spike-frontend';
const route = 'have-you-previously-been-in-a-marriage-or-civil-partnership';
const viewport = 'desktop';
let thing = '';
let state = 'default';

describe(`Page:: ${pageTitleEN}`, () => {

  beforeEach(() => {
    cy.viewport(env.desktop.viewport[0]);
    cy.visit(page);
    cy.injectAxe();
    cy.munchCookies();

    describe('Set up aliases', () => {
      Cypress.log({
        displayName: 'ALIASES',
        message: 'Set up aliases',
        name: 'setUpAliases',
      });

      // ADD_SOME_ALIASES

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

    it('checks form elements exist', () => {});

    it('checks error states are handled', () => {});

  });

  describe('Go all the wrong ways', () => {});

  describe('Go to the next page', () => {
    it('submits the form', () => {

      // ADD_SOME_NEXT_PAGE_CHECKS
      // NEXT_PAGE: previous-marriage-or-partnership-details

    });
  });

});
