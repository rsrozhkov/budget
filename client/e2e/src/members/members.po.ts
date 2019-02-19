import {browser, by, element, error} from 'protractor';

export class MembersPage {

  navigateTo() {
    return browser.get('/members');
  }

  getTitleText() {
    return element(by.css('app-members h3')).getText();
  }

  getAddMemberHeaderText() {
    return element(by.id('addMemberHeader')).getText();
  }

  getNameLabelText() {
    return element(by.id('nameLabel')).getText();
  }

  getNameInput() {
    return element(by.id('nameInput'));
  }

  setName(name) {
    this.getNameInput().sendKeys(name);
  }

  setNameOverMaxLen(maxLen) {
    let name;
    for (let i=0 , max = maxLen+1; i<max; i++) {
      name+='a';
    }
    this.setName(name);
  }

  getNameInputValue() {
    return this.getNameInput().getAttribute('value').then(function (name) {
      return name;
    });
  }

  getNameRequiredError() {
    return element(by.id('nameReqErr')).getText();
  }

  getNameLen() {
    return this.getNameInputValue().then(function (name) {
      return name.length;
    })
  }

  getNameLengthError() {
    return element(by.id('nameLenErr')).getText();
  }

  getNameFormatError() {
    return element(by.id('nameFormatErr')).getText();
  }

  getNameExistError() {
    return element(by.id('nameExistErr')).getText();
  }

  getAddButton() {
    return element(by.id('addButton'));
  }

  isAddButtonDisabled() {
    return this.getAddButton().getAttribute('disabled')
      .then(function (attr) {
        return attr;
      });
  }

  getMembersListHeaderText() {
    return element(by.id('listHeader')).getText();
  }

  getMembersListLength() {
    return element.all(by.css('ul')).all(by.css('li'))
      .then(function (list) {
        return list.length;
      })
  }

  getListElementByName(name) {
    return element(by.css('ul'))
      .element(by.cssContainingText('li', name));
  }

  isListContainsName(name) {
    return this.getListElementByName(name).isPresent();
  }

  getDeleteButtonByName(name) {
    return this.getListElementByName(name)
      .element(by.css('button'));
  }

  getAlertText(){
    browser.sleep(100);
    let alert = browser.switchTo().alert();
    let message = alert.getText();
    alert.accept();
    browser.switchTo().defaultContent();
    return message;
  }

  getMemberEditText(){
    return element(by.css('app-member-edit h3')).getText();
  }
}
