import {browser, by, element, error} from 'protractor';

export class AppPage {

  navigateTo() {
    return browser.get('/');
  }

  getTitleText() {
    return element(by.css('app-budget h3')).getText();
  }

  getBalanceString() {
    return element(by.id('totalBalance')).getText();
  }

  getBalanceValue() {
    return this.getBalanceString().then(function (value) {
      let balance;
      do {
        balance = value;
        value = value.replace(' ', '');
      } while (balance != value);
      return parseFloat(balance.replace(',', '.'));
    });
  }

  getDepositButton() {
    return element(by.id('depositButton'));
  }

  getWithdrawButton() {
    return element(by.id('withdrawButton'));
  }

  isTransactionFormPresent() {
    return element(by.css('app-budget .form')).isPresent();
  }

  getNameLabelText() {
    return element(by.id('nameLabel')).getText();
  }

  getNameSelector() {
    return element(by.id('select'));
  }

  getNamesListLen() {
    return element.all(by.id('select')).all(by.css('option'))
      .then(function (list) {
      return list.length;
    })
  }

  selectName(name) {
    this.getNameSelector()
      .element(by
        .cssContainingText('option', name))
      .click();
  }

  isNameSelected() {
    return this.getNameSelector()
      .getAttribute("ng-reflect-model");
  }

  getAmountLabelText() {
    return element(by.id('amountLabel')).getText();
  }

  getAmountInput() {
    return element(by.id('amountInput'));
  }

  setAmount(amount) {
    this.getAmountInput()
      .sendKeys(amount.toString()
        .replace('.',','));
  }

  getAmount() {
    return this.getAmountInput().getAttribute('value');
  }

  getAmountRequiredError() {
    return element(by.id('amountReqErr'));
  }

  getAmountMinError() {
    return element(by.id('amountMinErr'));
  }

  getAmountFormatError() {
    return element(by.id('amountFormatErr'));
  }

  getNotEnoughMoneyError() {
    return element(by.id('notEnoughMoneyErr'));
  }

  getDepositCommentLabel() {
    return element(by.id('depositCommentLabel'));
  }

  getWithdrawCommentLabel() {
    return element(by.id('withdrawCommentLabel'));
  }

  getCommentInput() {
    return element(by.id('commentInput'));
  }

  setComment(comment) {
    this.getCommentInput().sendKeys(comment);
  }

  setCommentOverMaxLen(maxLen) {
    let comment;
    for (let i=0 , max = maxLen+1; i<max; i++) {
      comment+='a';
    }
    this.setComment(comment);
  }

  getComment() {
    return this.getCommentInput().getAttribute('value').then(function (comment) {
      return comment;
    });
  }

  getCommentLen() {
    return this.getComment().then(function (comment) {
      return comment.length;
    })
  }

  getCommentRequiredError() {
    return element(by.id('commentReqErr'));
  }

  getCommentLenError() {
    return element(by.id('commentLenErr'));
  }

  getCommentFormatError() {
    return element(by.id('commentFormatErr'));
  }

  getGoBackButton() {
    return element(by.id('goBackButton'));
  }

  getOkButton() {
    return element(by.id('okButton'));
  }

  isOkButtonDisabled() {
    return this.getOkButton().getAttribute('disabled')
      .then(function (attr) {
        return attr;
      });
  }

}
