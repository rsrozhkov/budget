import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule }    from '@angular/common/http';

import { AppComponent } from './app.component';
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import { MembersComponent } from './components/members/members.component';
import { MemberEditComponent } from './components/member-edit/member-edit.component';
import { AppRoutingModule } from './app-routing.module';
import { registerLocaleData } from '@angular/common';
import localeRu from '@angular/common/locales/ru';
import { BudgetComponent } from './components/budget/budget.component';
import { ReportComponent } from './components/report/report.component';
import {UniqueNameValidatorDirective} from "./directives/name.directive";
import { ChartComponent } from './components/chart/chart.component';

registerLocaleData(localeRu, 'ru');

@NgModule({
  declarations: [
    AppComponent,
    MembersComponent,
    MemberEditComponent,
    BudgetComponent,
    ReportComponent,
    UniqueNameValidatorDirective,
    ChartComponent,
  ],
  imports: [
    BrowserModule,
    FormsModule,
    HttpClientModule,
    AppRoutingModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
