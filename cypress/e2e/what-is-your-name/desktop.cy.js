/* eslint-disable no-undef, no-unused-vars, cypress/no-unnecessary-waiting, sonarjs/no-duplicate-string, sonarjs/no-identical-functions */
const port = 11300;
const serviceID = 'fill-online/get-your-national-insurance-number-by-post';
const pageSlug = `${serviceID}/what-is-your-name`;
const nextSlug = `${serviceID}/do-you-have-a-previous-name`;
const thisPage = `http://localhost:${port}/${pageSlug}`;
const nextPage = `http://localhost:${port}/${nextSlug}`;
const pageTitleEN = 'What is your name?';
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
const route = 'what-is-your-name';
const viewport = 'desktop';
let thing = '';
let state = 'default';

describe(`Page:: ${pageTitleEN}`, () => {
  beforeEach(() => {
    cy.viewport(env.desktop.viewport[0]);
    cy.visit(pageSlug);
    cy.injectAxe();
    cy.munchCookies().then((response) => {
      cy.getCookie('mdtp').should('exist');
      cy.getCookie('mdtpdi').should('exist');
    });

    describe('Set up aliases', () => {
      Cypress.log({
        displayName: 'ALIASES',
        message: 'Set up aliases',
        name: 'setUpAliases',
      });

      cy.get('[data-spec="what_is_your_name__heading_with_caption"]').as(
        'what_is_your_name__heading_with_caption'
      );
      cy.get('[data-spec="what_is_your_name__honorific_prefix"]').as(
        'what_is_your_name__honorific_prefix'
      );
      cy.get('[data-spec="what_is_your_name__given_name"]').as(
        'what_is_your_name__given_name'
      );
      cy.get('[data-spec="what_is_your_name__additional_name').as(
        'what_is_your_name__additional_name'
      );
      cy.get('[data-spec="what_is_your_name__family_name"]').as(
        'what_is_your_name__family_name'
      );
      cy.get('[data-spec="what_is_your_name__continue"]').as(
        'what_is_your_name__continue'
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
      cy.get('@what_is_your_name__honorific_prefix').should('exist');
      cy.get('@what_is_your_name__given_name').should('exist');
      cy.get('@what_is_your_name__additional_name').should('exist');
      cy.get('@what_is_your_name__family_name').should('exist');
      cy.get('@what_is_your_name__continue').should('exist');
    });

    it('checks error states are handled', () => {
      cy.get('@what_is_your_name__continue').click();
      cy.get('.govuk-error-summary').as('errorSummary');
      cy.get('@errorSummary').checkGovukErrorSummary();
      cy.get('@what_is_your_name__given_name').checkGovukErrorMessage();
      cy.get('@what_is_your_name__family_name').checkGovukErrorMessage();
      cy.checkPageTitle('hasError');
    });
  });

  describe('Go to the next page', () => {
    it('submits the form', () => {
      cy.session([userUuid, firstName, lastName, pageSlug], () => {
        cy.visit(pageSlug);
        cy.get('@what_is_your_name__honorific_prefix').type(
          user.username.title
        );
        cy.get('@what_is_your_name__given_name').type(
          user.username.firstName
        );
        cy.get('@what_is_your_name__additional_name').type(
          user.username.middleNames
        );
        cy.get('@what_is_your_name__family_name').type(
          user.username.lastName
        );
        cy.get('@what_is_your_name__continue').click();
        cy.url().should('contain', '/do-you-have-a-previous-name');
      });
    });
  });
});
