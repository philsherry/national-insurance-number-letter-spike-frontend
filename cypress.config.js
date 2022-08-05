/// <reference types="cypress" />
/* eslint-disable no-unused-vars */

const shell = require('shelljs');
const istanbul = require('istanbul-lib-coverage');
const { join } = require('path');
const { existsSync, mkdirSync, readFileSync, writeFileSync } = require('fs');
const { lighthouse, pa11y, prepareAudit } = require('cypress-audit');
const chai = require('chai');

const { defineConfig } = require('cypress');

module.exports = defineConfig({
  e2e: {
    baseUrl: 'http://localhost:11300/',
    chromeWebSecurity: false,
    experimentalSessionAndOrigin: true,
    env: {
      browserPermissions: {
        camera: 'block',
        cookies: 'allow',
        geolocation: 'allow',
        images: 'allow',
        javascript: 'allow',
        microphone: 'block',
        notifications: 'allow',
        plugins: 'ask',
        popups: 'ask',
      },
      FAIL_FAST_ENABLED: false,
      FAIL_FAST_STRATEGY: 'run',
    },
    setupNodeEvents(on, config) {

      // console.log(config) // see everything in here!

      chai.use(require('chai-string'));

      // bind to the event we care about
      // these are standard folder and file names used by NYC tools
      const outputFolder = '.nyc_output';
      const coverageFolder = join(process.cwd(), outputFolder);
      const nycFilename = join(coverageFolder, 'out.json');

      require('cypress-log-to-output').install(on);
      require('cypress-fail-fast/plugin')(on, config);
      require('@cypress/code-coverage/task')(on, config);
      // // tell Cypress to use .babelrc file
      // // and instrument the specs files
      // // only the extra application files will be instrumented
      // // not the spec files themselves
      // on('file:preprocessor', require('@cypress/code-coverage/use-babelrc'));

      // or, if there is already a before:browser:launch handler, use .browserLaunchHandler inside of it
      // @see https://github.com/flotwig/cypress-log-to-output/issues/5
      on('before:browser:launch', (browser = {}, launchOptions) => {
        prepareAudit(launchOptions);
      });

      if (!existsSync(coverageFolder)) {
        mkdirSync(coverageFolder);
        console.log('created folder %s for output coverage', coverageFolder);
      }

      // Log things to the console
      on('task', {
        /**
         * @link https://github.com/mfrachet/cypress-audit
         **/
        lighthouse: lighthouse((lighthouseReport) => {
          console.log(lighthouseReport); // raw lighthouse reports
        }),

        pa11y: pa11y((pa11yReport) => {
          console.log(pa11yReport); // raw pa11y reports
        }),

        log(message) {
          console.log(message);

          return null;
        },

        table(message) {
          console.table(message);

          return null;
        },

        /**
         * https://github.com/bahmutov/demo-battery-api/blob/coverage-step/cypress/plugins/index.js
         * Clears accumulated code coverage information.
         *
         * Interactive mode with "cypress open"
         *    - running a single spec or "Run all specs" needs to reset coverage
         * Headless mode with "cypress run"
         *    - runs EACH spec separately, so we cannot reset the coverage
         *      or we will lose the coverage from previous specs.
         */
        resetCoverage({ isInteractive }) {
          if (isInteractive) {
            console.log('reset code coverage in interactive mode');
            const coverageMap = istanbul.createCoverageMap({});
            writeFileSync(nycFilename, JSON.stringify(coverageMap, null, 2));
          }
          /*
        Else:
          in headless mode, assume the coverage file was deleted
          before the `cypress run` command was called
          example: rm -rf .nyc_output || true
      */

          return null;
        },

        /**
         * Combines coverage information from single test
         * with previously collected coverage.
         */
        combineCoverage(coverage) {
          const previous = existsSync(nycFilename)
            ? JSON.parse(readFileSync(nycFilename))
            : istanbul.createCoverageMap({});
          const coverageMap = istanbul.createCoverageMap(previous);
          coverageMap.merge(coverage);
          writeFileSync(nycFilename, JSON.stringify(coverageMap, null, 2));
          console.log('wrote coverage file %s', nycFilename);

          return null;
        },

        /**
         * Saves coverage information as a JSON file and calls
         * NPM script to generate HTML report
         */
        coverageReport() {
          console.log('saving coverage report');
          const localNodeBinary = shell.which('node');

          return (
            (shell.config.execPath = localNodeBinary.stdout),
            shell.exec('npm run coverage:report')
          );
        },
      });

      // IMPORTANT return the updated config object
      return config;
    },
    reporter: 'mocha-multi-reporters',
    reporterOptions: {
      configFile: 'cypress-reporters.config.js',
      html: true,
      json: true,
      overwrite: false,
      reportDir: 'cypress/results',
    },
    retries: {
      openMode: 1,
      runMode: 1,
    },
    viewportHeight: 900,
    viewportWidth: 1440,
  },
});
