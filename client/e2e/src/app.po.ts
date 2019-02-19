import {browser, by, element, promise} from 'protractor';

export class AppPage {

  navigateTo() {
    return browser.get('/');
  }

  getTitleText() {
    return element(by.css('app-root h1')).getText();
  }

  getNavHomeButton() {
    return element(by.css('[routerlink=""]'));
  }

  getNavChartButton() {
    return element(by.css('[routerlink="/chart"]'));
  }

  getNavReportButton() {
    return element(by.css('[routerlink="/report"]'));
  }

  getNavMembersButton() {
    return element(by.css('[routerlink="/members"]'));
  }

  getBudgetText() {
    return element(by.css('app-budget h3')).getText();
  }

  getChartText() {
    return element(by.css('app-chart h3')).getText();
  }

  getReportText() {
    return element(by.css('app-report h3')).getText();
  }

  getMembersText() {
    return element(by.css('app-members h3')).getText();
  }

}
