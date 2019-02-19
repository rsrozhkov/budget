import {browser, by, element, error} from 'protractor';

export class AppPage {

  navigateTo() {
    return browser.get('/report');
  }

  getTitleText() {
    return element(by.css('app-report h3')).getText();
  }

  getStartDateLabelText() {
    return element(by.id('startDateLabel')).getText();
  }

  getStartDateInput() {
    return element(by.id('startDateInput'));
  }

  getStartDateInputValue() {
    return this.getStartDateInput().getAttribute('value').then(function (date) {
      return date;
    });
  }

  getEndDateLabelText() {
    return element(by.id('endDateLabel')).getText();
  }

  getEndDateInput() {
    return element(by.id('endDateInput'));
  }

  getEndDateInputValue() {
    return this.getEndDateInput().getAttribute('value').then(function (date) {
      return date;
    });
  }

  getValidationError() {
    return element(by.id('validationError'));
  }

  getLoadButton() {
    return element(by.id('loadButton'));
  }

  isLoadButtonDisabled() {
    return this.getLoadButton().getAttribute('disabled')
      .then(function (attr) {
        return attr;
      });
  }

  getColumnsQuantity() {
    return element.all(by.css('table')).all(by.css('th')).then(function (list) {
      return list.length;
    });
  }

  getRowsQuantity() {
    return element.all(by.css('table')).all(by.css('tr')).then(function (list) {
      return list.length;
    });
  }

  isColumnPresent(name) {
    return element(by.css('table'))
      .element(by.cssContainingText('th', name)).isPresent();
  }

}
