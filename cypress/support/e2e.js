/* eslint-disable no-unused-vars */
/// <reference types="cypress" />
import './commands';
import '@cypress/code-coverage/support';
import 'cypress-axe';
import 'cypress-fail-fast';
import failOnConsoleError, {
  consoleType,
} from 'cypress-fail-on-console-error';

/**
 * https://github.com/nils-hoyer/cypress-fail-on-console-error
 */
const config = {
  // excludeMessages: ['foo', '^some bar-regex.*'],
  includeConsoleTypes: [
    consoleType.ERROR,
    consoleType.WARN,
    consoleType.INFO,
  ],
  cypressLog: true,
};

failOnConsoleError(config);

/**
 * Console errors that we allow.
 **/
Cypress.on('uncaught:exception', (err, runnable) => {
  if (
    err.message.includes(
      'http://localhost:12345/tracking-consent/tracking.js'
    )
  ) {
    return false;
  }

  if (
    err.message.includes('Failed to load resource: net::ERR_EMPTY_RESPONSE')
  ) {
    return false;
  }
});

before(() => {
  // we need to reset the coverage when running
  // in the interactive mode, otherwise the counters will
  // keep increasing every time we rerun the tests
  cy.task('resetCoverage', {
    isInteractive: Cypress.config('isInteractive'),
  });
});

afterEach(() => {
  // save coverage after each test
  // because the entire "window" object is about
  // to be recycled by Cypress before next test
  cy.window().then((win) => {
    if (win.__coverage__) {
      cy.task('combineCoverage', win.__coverage__);
    }
  });
});

after(() => {
  // when all tests finish, generate the coverage report
  cy.task('coverageReport');
});
