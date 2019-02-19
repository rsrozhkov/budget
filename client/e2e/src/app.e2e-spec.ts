import { AppPage } from './app.po';

describe('тесты app.component', () => {
  let page: AppPage;

  beforeAll(() => {
    page = new AppPage();
    page.navigateTo();
  });

  it('отображает название приложения', () => {
    expect(page.getTitleText()).toEqual('СЕМЕЙНЫЙ БЮДЖЕТ');
  });

  it('отображаеть кнопку \"Главная\"', () => {
    expect(page.getNavHomeButton().getText()).toEqual('Главная');
  });

  it('отображает кнопку \"График бюджета\"', () => {
    expect(page.getNavChartButton().getText()).toEqual('График бюджета');
  });

  it('отображает кнопку \"Выгрузка отчёта\"', () => {
    expect(page.getNavReportButton().getText()).toEqual('Выгрузка отчёта');
  });

  it('отображает кнопку \"Члены семьи\"', () => {
    expect(page.getNavMembersButton().getText()).toEqual('Члены семьи');
  });

  it('остаётся на странице \"Главная\"', () => {
    page.getNavHomeButton().click();
    expect(page.getBudgetText()).toEqual('В семейном бюджете:');
  });

  it('переходит на страницу \"График бюджета\"', () => {
    page.getNavChartButton().click();
    expect(page.getChartText()).toEqual('График бюджета:');
  });

  it('переходит на страницу \"Выгрузка отчёта\"', () => {
    page.getNavReportButton().click();
    expect(page.getReportText()).toEqual('Отчёт по затратам:');
  });

  it('переходит на страницу \"Члены семьи\"', () => {
    page.getNavMembersButton().click();
    expect(page.getMembersText()).toEqual('Члены семьи:');
  });

  it('переходит на страницу \"Главная\"', () => {
    page.getNavHomeButton().click();
    expect(page.getBudgetText()).toEqual('В семейном бюджете:');
  });

});
