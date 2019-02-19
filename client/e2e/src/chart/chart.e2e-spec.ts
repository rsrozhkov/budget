import { AppPage } from './chart.po';

describe('тесты chart.component', () => {
  let page: AppPage;

  beforeAll(() => {
    page = new AppPage();
    page.navigateTo();
  });

  it('отображает заголовок компонента', () => {
    expect(page.getTitleText()).toEqual('График бюджета:');
  });

  it('график загружается', () => {
    expect(page.isChartPresent()).toBeTruthy();
  });

});
