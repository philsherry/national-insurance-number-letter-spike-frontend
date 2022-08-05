/* eslint-disable no-undef, no-unused-vars, cypress/no-unnecessary-waiting, sonarjs/no-duplicate-string, sonarjs/no-identical-functions */
const page = 'fill-online/get-your-national-insurance-number-by-post';
const pageTitleEN = 'Get your National Insurance number by post';
const pageTitleCY = '';
const env = require('../../fixtures/env.json');
const user = require('../../fixtures/user.json');
const { terminalLog } = require('../../plugins/hmrc');

/**
 * @screenshotFormat png
 * @screenshotFileName `${service}--${route}--${viewport}--${thing}--${state}`
 **/
const service = 'national-insurance-number-letter-spike-frontend';
const route = 'start';
const viewport = 'mobile';
let thing = '';
let state = 'default';

describe(`Page:: ${pageTitleEN}`, () => {

  before(() => {
    cy.viewport(env.mobile.viewport[0]);
  });

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

      cy.get('[data-spec="start__index_heading"]').as('start__index_heading');
      cy.get('[data-spec="start__what_information"]').as(
        'start__what_information'
      );
      cy.get('[data-spec="start__what_information__list_1"]').as(
        'start__what_information__list_1'
      );
      cy.get('[data-spec="start__what_information__list_2"]').as(
        'start__what_information__list_2'
      );
      cy.get('[data-spec="start__before_you_start"]').as(
        'start__before_you_start'
      );
      cy.get('[data-spec="start__other_ways"]').as('start__other_ways');
      cy.get('[data-spec="start__other_ways__link_1"]').as(
        'start__other_ways__link_1'
      );
      cy.get('[data-spec="start__other_ways__link_2"]').as(
        'start__other_ways__link_2'
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
    // Page headings
    it('checks page headings are followed by content', () => {
      cy.get('@start__index_heading')
        .should('exist')
        .should('have.class', 'govuk-heading-xl');

      // Welsh check here whenever we add Welsh content.
      // cy.root().should('have.attr', 'lang', 'cy');
      // See: cypress/support/govuk-cypress/hmrc--welsh-translations.js

      cy.get('@start__index_heading')
        .nextUntil('h2')
        .should('have.length', 1); // 2, with Welsh content.

      cy.get('@start__what_information')
        .nextUntil('h2')
        .should('have.length', 3);

      cy.get('@start__before_you_start')
        .nextUntil('.govuk-button--start')
        .should('have.length', 2);

      cy.get('@start__other_ways').nextAll().should('have.length', 5); // allow for the Feedback link.
    });

    // links
    it('checks links are correct', () => {
      cy.get('@start__other_ways__link_1').should(
        'have.attr',
        'href',
        'https://www.gov.uk/personal-tax-account'
      );

      cy.get('@start__other_ways__link_2').should(
        'have.attr',
        'href',
        'https://www.gov.uk/apply-national-insurance-number'
      );
    });

    // lists
    it('checks lists', () => {
      cy.get('@start__what_information__list_1')
        .should('exist')
        .should('have.class', 'govuk-list--bullet');

      cy.get('@start__what_information__list_1')
        .find('li')
        .then(($lis) => {
          expect($lis, '4 items').to.have.length(4);
        });

      cy.get('@start__what_information__list_2')
        .should('exist')
        .should('have.class', 'govuk-list--bullet');

      cy.get('@start__what_information__list_1')
        .find('li')
        .then(($lis) => {
          expect($lis, '4 items').to.have.length(4);
        });
    });
  });

  describe('Go all the wrong ways', () => {});

  describe('Go to the next page', () => {
    it('submits the form', () => {
      cy.get('@start__before_you_start').click();
    });
  });

});
