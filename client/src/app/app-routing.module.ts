import { NgModule } from '@angular/core';
import {RouterModule, Routes} from "@angular/router";
import {MembersComponent} from "./components/members/members.component";
import {MemberEditComponent} from "./components/member-edit/member-edit.component";
import {BudgetComponent} from "./components/budget/budget.component";
import {ReportComponent} from "./components/report/report.component";
import {ChartComponent} from "./components/chart/chart.component";

const routes: Routes = [
  {path: '', redirectTo: '/budget', pathMatch: 'full'},
  {path: 'budget', component: BudgetComponent},
  {path: 'chart', component: ChartComponent},
  {path: 'report', component: ReportComponent},
  {path: 'members', component: MembersComponent},
  {path: 'members/:id', component: MemberEditComponent},
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes)
  ],
  exports: [
    RouterModule
  ]
})

export class AppRoutingModule { }
