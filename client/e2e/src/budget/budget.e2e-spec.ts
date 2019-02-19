import { AppPage } from './budget.po';
import {protractor} from "protractor/built/ptor";

describe('тесты budget.component', () => {
  let page: AppPage;
  let existMemberName: string;
  let testAmount: number;
  let testComment:string;

  beforeAll(() => {
    page = new AppPage();
    page.navigateTo();
    existMemberName = 'Василий Чапаев';
    testAmount = 100.01;
    testComment = 'Test comment';
  });

  it('отображает заголовок компонента', () => {
    expect(page.getTitleText()).toEqual('В семейном бюджете:');
  });

  it('отображает не отрицательный баланс', () => {
    expect(page.getBalanceValue()).toBeGreaterThanOrEqual(0);
  });

  it('отображает символ валюты "₽"', () => {
    expect(page.getBalanceString()).toContain('₽');
  });

  it('отображает кнопку "Внести"', () => {
    expect(page.getWithdrawButton().getText()).toEqual('Потратить');
  });

  it('отображает кнопку "Потратить"', () => {
    expect(page.getWithdrawButton().getText()).toEqual('Потратить');
  });

  it('транзакция: форма скрыта', () => {
    expect(page.isTransactionFormPresent()).toBeFalsy();
  });

  it('транзакция: форма открывается кнопкой "Внести"', () => {
    page.navigateTo();
    page.getDepositButton().click();
    expect(page.isTransactionFormPresent()).toBeTruthy();
  });

  it('транзакция: форма открывается кнопкой "Потратить"', () => {
    page.navigateTo();
    page.getWithdrawButton().click();
    expect(page.isTransactionFormPresent()).toBeTruthy();
  });

  it('внести: форма отображается верно', () => {
    page.navigateTo();
    page.getDepositButton().click();

    expect(page.getNameLabelText()).toEqual('Имя:');
    expect(page.getNameSelector().isPresent()).toBeTruthy();

    expect(page.getAmountLabelText()).toEqual('Сумма:');
    expect(page.getAmountInput().isPresent()).toBeTruthy();
    expect(page.getAmountRequiredError().isPresent()).toBeFalsy();
    expect(page.getAmountMinError().isPresent()).toBeFalsy();
    expect(page.getAmountFormatError().isPresent()).toBeFalsy();
    expect(page.getNotEnoughMoneyError().isPresent()).toBeFalsy();

    expect(page.getDepositCommentLabel().getText()).toEqual('Откуда:');
    expect(page.getWithdrawCommentLabel().isPresent()).toBeFalsy();
    expect(page.getCommentInput().isPresent).toBeTruthy();
    expect(page.getCommentRequiredError().isPresent()).toBeFalsy();
    expect(page.getCommentLenError().isPresent()).toBeFalsy();
    expect(page.getCommentFormatError().isPresent()).toBeFalsy();

    expect(page.getGoBackButton().getText()).toEqual('Отмена');
    expect(page.getOkButton().getText()).toEqual('ОК');
    expect(page.isOkButtonDisabled()).toBeTruthy();

  });

  it('потратить: форма отображается верно', () => {
    page.navigateTo();
    page.getWithdrawButton().click();
    expect(page.getNameLabelText()).toEqual('Имя:');
    expect(page.getNameSelector().isPresent()).toBeTruthy();

    expect(page.getAmountLabelText()).toEqual('Сумма:');
    expect(page.getAmountInput().isPresent()).toBeTruthy();
    expect(page.getAmountRequiredError().isPresent()).toBeFalsy();
    expect(page.getAmountMinError().isPresent()).toBeFalsy();
    expect(page.getAmountFormatError().isPresent()).toBeFalsy();
    expect(page.getNotEnoughMoneyError().isPresent()).toBeFalsy();

    expect(page.getDepositCommentLabel().isPresent()).toBeFalsy();
    expect(page.getWithdrawCommentLabel().getText()).toEqual('На что:');
    expect(page.getCommentInput().isPresent).toBeTruthy();
    expect(page.getCommentRequiredError().isPresent()).toBeFalsy();
    expect(page.getCommentLenError().isPresent()).toBeFalsy();
    expect(page.getCommentFormatError().isPresent()).toBeFalsy();

    expect(page.getGoBackButton().getText()).toEqual('Отмена');
    expect(page.getOkButton().getText()).toEqual('ОК');
    expect(page.isOkButtonDisabled()).toBeTruthy();
  });

  it('внести: форма закрывается кнопкой "Отмена"', () => {
    page.navigateTo();
    page.getDepositButton().click();
    expect(page.isTransactionFormPresent()).toBeTruthy();
    page.getGoBackButton().click();
    expect(page.isTransactionFormPresent()).toBeFalsy();
  });

  it('потратить: форма закрывается кнопкой "Отмена"', () => {
    page.navigateTo();
    page.getWithdrawButton().click();
    expect(page.isTransactionFormPresent()).toBeTruthy();
    page.getGoBackButton().click();
    expect(page.isTransactionFormPresent()).toBeFalsy();
  });

  it('транзакция: внести/портатить переключаются', () => {
    page.navigateTo();
    page.getDepositButton().click();
    expect(page.getDepositCommentLabel().isPresent()).toBeTruthy();
    expect(page.getWithdrawCommentLabel().isPresent()).toBeFalsy();

    page.getWithdrawButton().click();
    expect(page.getDepositCommentLabel().isPresent()).toBeFalsy();
    expect(page.getWithdrawCommentLabel().isPresent()).toBeTruthy();

    page.getDepositButton().click();
    expect(page.getDepositCommentLabel().isPresent()).toBeTruthy();
    expect(page.getWithdrawCommentLabel().isPresent()).toBeFalsy();
  });

  it('внести: список членов семьи загружается', () => {
    page.navigateTo();
    page.getDepositButton().click();
    expect(page.getNamesListLen()).toBeGreaterThan(1);
  });

  it('внести: существующий член семьи выбирается', () => {
    page.navigateTo();
    page.getDepositButton().click();
    page.selectName(existMemberName);
    expect(page.isNameSelected()).toContain('Object');
  });

  it('внести: сумма вводится', () => {
    page.navigateTo();
    page.getDepositButton().click();
    page.setAmount(testAmount);
    expect(page.getAmount()).toEqual(testAmount.toString());
  });

  it('внести: комментарий вводится', () => {
    page.navigateTo();
    page.getDepositButton().click();
    page.setComment(testComment);
    expect(page.getComment()).toEqual(testComment);
  });

  it('внести-валидация: член семьи', () => {
    page.navigateTo();
    page.getDepositButton().click();
    page.setAmount(testAmount);
    page.setComment(testComment);
    expect(page.isOkButtonDisabled()).toBeTruthy();
    page.selectName(existMemberName);
    expect(page.isOkButtonDisabled()).toBeFalsy();
  });

  it('внести-валидация: сумма', () => {
    page.navigateTo();
    page.getDepositButton().click();
    page.selectName(existMemberName);
    page.setComment(testComment);
    expect(page.isOkButtonDisabled()).toBeTruthy();

    page.setAmount(testAmount);
    expect(page.isOkButtonDisabled()).toBeFalsy();

    page.getAmountInput().clear();
    expect(page.getAmountRequiredError().isPresent()).toBeTruthy();
    expect(page.isOkButtonDisabled()).toBeTruthy();

    page.getAmountInput().clear();
    page.setAmount(0.009);
    //TODO разобраться почему не работает проверка на минимум
    //expect(page.getAmountMinError().isPresent()).toBeTruthy();
    expect(page.isOkButtonDisabled()).toBeTruthy();

    page.getAmountInput().clear();
    page.setAmount('-12,0,ф');
    //TODO разобраться почему в разных браузерах валидация работает по-разному
    //expect(page.getAmountFormatError().isPresent()).toBeTruthy();
    expect(page.isOkButtonDisabled()).toBeTruthy();
  });

  it('внести-валидация: коментарий', () => {
    page.navigateTo();
    page.getDepositButton().click();
    page.selectName(existMemberName);
    page.setAmount(testAmount);
    expect(page.isOkButtonDisabled()).toBeTruthy();

    page.setComment(testComment);
    expect(page.isOkButtonDisabled()).toBeFalsy();

    page.getCommentInput().clear();
    page.setComment('a');
    page.getCommentInput().sendKeys(protractor.Key.BACK_SPACE);
    expect(page.getCommentRequiredError().isPresent()).toBeTruthy();
    expect(page.isOkButtonDisabled()).toBeTruthy();

    page.getCommentInput().clear();
    page.setComment('a2');
    expect(page.getCommentLenError().isPresent()).toBeTruthy();
    expect(page.isOkButtonDisabled()).toBeTruthy();

    page.getCommentInput().clear();
    page.setCommentOverMaxLen(150);
    expect(page.getCommentLen()).toBe(150);
    expect(page.isOkButtonDisabled()).toBeFalsy();

    page.getCommentInput().clear();
    page.setComment('aaa \n aaa');
    expect(page.getComment()).toEqual('aaa  aaa');
    expect(page.isOkButtonDisabled()).toBeFalsy();
  });

  it('потратить: список членов семьи загружается', () => {
    page.navigateTo();
    page.getWithdrawButton().click();
    expect(page.getNamesListLen()).toBeGreaterThan(1);
  });

  it('потратить: существующий член семьи выбирается', () => {
    page.navigateTo();
    page.getWithdrawButton().click();
    page.selectName(existMemberName);
    expect(page.isNameSelected()).toContain('Object');
  });

  it('потратить: сумма вводится', () => {
    page.navigateTo();
    page.getWithdrawButton().click();
    page.setAmount(testAmount);
    expect(page.getAmount()).toEqual(testAmount.toString());
  });

  it('потратить: комментарий вводится', () => {
    page.navigateTo();
    page.getWithdrawButton().click();
    page.setComment(testComment);
    expect(page.getComment()).toEqual(testComment);
  });

  it('потратить-валидация: член семьи', () => {
    page.navigateTo();
    page.getWithdrawButton().click();
    page.setAmount(testAmount);
    page.setComment(testComment);
    expect(page.isOkButtonDisabled()).toBeTruthy();
    page.selectName(existMemberName);
    expect(page.isOkButtonDisabled()).toBeFalsy();
  });

  it('потратить-валидация: сумма', () => {
    page.navigateTo();
    page.getWithdrawButton().click();
    page.selectName(existMemberName);
    page.setComment(testComment);
    expect(page.isOkButtonDisabled()).toBeTruthy();

    page.setAmount(testAmount);
    expect(page.isOkButtonDisabled()).toBeFalsy();

    page.getAmountInput().clear();
    expect(page.getAmountRequiredError().isPresent()).toBeTruthy();
    expect(page.isOkButtonDisabled()).toBeTruthy();

    page.getAmountInput().clear();
    page.setAmount(0.009);
    //TODO разобраться почему не работает проверка на минимум
    //expect(page.getAmountMinError().isPresent()).toBeTruthy();
    expect(page.isOkButtonDisabled()).toBeTruthy();

    page.getAmountInput().clear();
    page.setAmount('-12,0,ф');
    //TODO разобраться почему в разных браузерах валидация работает по-разному
    //expect(page.getAmountFormatError().isPresent()).toBeTruthy();
    expect(page.isOkButtonDisabled()).toBeTruthy();
  });

  it('потратить-валидация: коментарий', () => {
    page.navigateTo();
    page.getWithdrawButton().click();
    page.selectName(existMemberName);
    page.setAmount(testAmount);
    expect(page.isOkButtonDisabled()).toBeTruthy();

    page.setComment(testComment);
    expect(page.isOkButtonDisabled()).toBeFalsy();

    page.getCommentInput().clear();
    page.setComment('a');
    page.getCommentInput().sendKeys(protractor.Key.BACK_SPACE);
    expect(page.getCommentRequiredError().isPresent()).toBeTruthy();
    expect(page.isOkButtonDisabled()).toBeTruthy();

    page.getCommentInput().clear();
    page.setComment('a2');
    expect(page.getCommentLenError().isPresent()).toBeTruthy();
    expect(page.isOkButtonDisabled()).toBeTruthy();

    page.getCommentInput().clear();
    page.setCommentOverMaxLen(150);
    expect(page.getCommentLen()).toBe(150);
    expect(page.isOkButtonDisabled()).toBeFalsy();

    page.getCommentInput().clear();
    page.setComment('aaa \n aaa');
    expect(page.getComment()).toEqual('aaa  aaa');
    expect(page.isOkButtonDisabled()).toBeFalsy();
  });

  it('внести: сумма добавляется', () => {
    page.navigateTo();
    let expectedBalance = page.getBalanceValue().then(function (balance) {
      return balance+testAmount;
    });
    page.getDepositButton().click();
    page.selectName(existMemberName);
    page.setAmount(testAmount);
    page.setComment(testComment);
    page.getOkButton().click();
    expect(page.getBalanceValue()).toEqual(expectedBalance);
  });

  it('потратить: сумма уменьшается', () => {
    page.navigateTo();
    let expectedBalance = page.getBalanceValue().then(function (balance) {
      return balance-testAmount;});
    page.getWithdrawButton().click();
    page.selectName(existMemberName);
    page.setAmount(testAmount);
    page.setComment(testComment);
    page.getOkButton().click();
    expect(page.getBalanceValue()).toEqual(expectedBalance);
  });

  it('потратить: сумма больше баланса', () => {
    page.navigateTo();
    let expectedBalance = page.getBalanceValue();
    let amount = page.getBalanceValue().then(function (balance) {
      return balance+0.01;});
    page.getWithdrawButton().click();
    page.selectName(existMemberName);
    page.setAmount(amount);
    page.setComment(testComment);
    page.getOkButton().click();
    expect(page.getNotEnoughMoneyError().isPresent).toBeTruthy();
    expect(page.getBalanceValue()).toEqual(expectedBalance);
  });

});
