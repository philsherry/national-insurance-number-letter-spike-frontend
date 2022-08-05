/* eslint-disable no-unused-vars */
/// <reference types="cypress" />
/**
 * @author: @philsherry
 * @function checkHmrcPageHeading
 * @description Cypress function to check the HMRC Page Heading pattern.
 * @example cy.checkHmrcPageHeading();
 * @example cy.checkHmrcPageHeading('hasPrefix', 'Mae’r adran hon yn');
 * @link https://design.tax.service.gov.uk/hmrc-design-patterns/page-heading
 **/

/**
 * @fixture One:
 * <header class="hmrc-page-heading" data-component="hmrc_page_heading" data-spec="what_is_your_name__heading_with_caption">
 *   <h1 class="govuk-heading-xl" data-component="hmrc_page_heading__h1" id="index-heading">
 *     What is your full name?
 *   </h1>
 *   <p class="govuk-caption-xl hmrc-caption" data-component="hmrc_page_heading__p">
 *     <span data-aria-hidden aria-hidden="true">
 *       Personal details
 *     </span>
 *     <span data-visually-hidden class="govuk-visually-hidden">
 *       This section is Personal details
 *     </span>
 *   </p>
 * </header>
 **/

/**
 * @fixture Two:
 * <legend class="govuk-fieldset__legend govuk-fieldset__legend--xl">
 *   <h1 class="govuk-fieldset__heading hmrc-page-heading govuk-!-margin-top-0 govuk-!-margin-bottom-0">
 *     Have you had any other names?
 *     <span class="hmrc-caption govuk-caption-xl">
 *       <span class="govuk-visually-hidden">
 *         This section is
 *       </span>Personal
 *       details
 *     </span>
 *   </h1>
 * </legend>
 **/

// -- This is a parent command --
Cypress.Commands.add('checkHmrcPageHeading', (subject, options) => {

});
