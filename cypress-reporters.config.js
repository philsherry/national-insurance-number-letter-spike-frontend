const { defineConfig } = require('cypress');

module.exports = defineConfig({
  e2e: {
    cypressMochawesomeReporterReporterOptions: {
      charts: true,
      embeddedScreenshots: true,
      inlineAssets: true,
      reportDir: 'cypress/reports',
      reportPageTitle: 'Accessibility audit'
    },
    mochaJunitReporterReporterOptions: {
      mochaFile: 'cypress/results/results-[hash].xml'
    },
    reporterEnabled: 'spec, json, cypress-mochawesome-reporter, mocha-junit-reporter, cypress-image-snapshot/reporter'
  }
});
