/* eslint-disable no-unused-vars */
/// <reference types="cypress" />
/**
 * @author: @philsherry
 * @function munchCookies
 * @description Cypress function to accept cookies and get the banner out of the way.
 * @example cy.munchCookies();
 * @link https://design.tax.service.gov.uk/hmrc-design-patterns/page-title
 * @fixture ```
 * <div class="cbanner-govuk-cookie-banner" role="region" aria-label="Cookies on HMRC services" data-nosnippet="">
 *   <div class="cbanner-govuk-cookie-banner__message cbanner-govuk-width-container">
 *     <div class="cbanner-govuk-grid-row">
 *       <div class="cbanner-govuk-grid-column-two-thirds">
 *         <h2 class="cbanner-govuk-cookie-banner__heading cbanner-govuk-heading-m">
 *           Cookies on HMRC services
 *         </h2>
 *         <div class="cbanner-govuk-cookie-banner__content">
 *           <p class="cbanner-govuk-body">
 *             We use some essential cookies to make our services work.
 *           </p>
 *           <p class="cbanner-govuk-body">
 *             We would like to set additional cookies so we can remember your settings,
 *             understand how people use our services and make improvements.
 *           </p>
 *         </div>
 *       </div>
 *     </div>
 *     <div class="cbanner-govuk-button-group">
 *       <button value="accept" type="button" name="cookies" class="cbanner-govuk-button" data-module="cbanner-govuk-button">
 *         Accept additional cookies
 *       </button>
 *       <button value="reject" type="button" name="cookies" class="cbanner-govuk-button" data-module="cbanner-govuk-button">
 *         Reject additional cookies
 *       </button>
 *       <a class="cbanner-govuk-link" href="http://localhost:12345/tracking-consent/cookie-settings">View cookies</a>
 *     </div>
 *   </div>
 * </div>
 * ```
 **/

// -- This is a parent command --
Cypress.Commands.add('munchCookies', (subject, options) => {
  const cbannerWrapper = '.cbanner-govuk-cookie-banner';
  const cbannerButtonAccept = '.cbanner-govuk-button[value="accept"]';
  const cbannerButtonAcceptText = 'Accept additional cookies';
  const cbannerButton = '.cbanner-govuk-button';
  const cbannerButtonText = 'Hide cookies message';

  cy.get(cbannerWrapper).then(($el) => {

    // check if the banner is present
    expect($el).to.exist;

    // click the accept button
    cy.get($el)
      .find(cbannerButtonAccept)
      .should('contain', cbannerButtonAcceptText)
      .click();

      // click the hide button
    cy.get($el)
      .find(cbannerButton)
      .should('contain', cbannerButtonText)
      .click();

      // check if the banner is gone
    cy.get(cbannerWrapper).should('not.exist');

  });

});
