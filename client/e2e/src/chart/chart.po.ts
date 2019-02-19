import {browser, by, element, error} from 'protractor';

export class AppPage {

  navigateTo() {
    return browser.get('/chart');
  }

  getTitleText() {
    return element(by.css('app-chart h3')).getText();
  }

  isChartPresent() {
    return element(by.id('lineChart')).isPresent();
  }

}
