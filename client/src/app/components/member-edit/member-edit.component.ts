import {Component, Input, OnInit} from '@angular/core';
import {Member} from "../../classes/member";
import {ActivatedRoute} from "@angular/router";
import {MemberService} from "../../services/member.service";
import { Location } from '@angular/common';

@Component({
  selector: 'app-member-edit',
  templateUrl: './member-edit.component.html',
  styleUrls: ['./member-edit.component.css']
})
export class MemberEditComponent implements OnInit {
  @Input()
  member: Member;
  private oldName: string;

  constructor(private route: ActivatedRoute,
              private memberService: MemberService,
              private location: Location) {}

  ngOnInit() {
    this.getMember();
  }

  /** Получает члена семьи с сервера по id */
  private getMember(): void {
    const id = +this.route.snapshot.paramMap.get('id');
    this.memberService.getMember(id)
      .subscribe(member => {
        this.member = member;
        this.oldName = member.name;
      });
  }

  /** Отмена действий с формой */
  private goBack(): void {
    this.location.back();
  }

  /** Если новая пара имя+фамилия не совпадает с полученными вначале,
   * тогда отправляет на сервер обновленного члена семьи */
  private save(): void {
    if (this.isTheSameName(this.member.name)) this.goBack();
    else this.memberService.updateMember(this.member)
      .subscribe(() => this.goBack());
  }

  /** Сравнивает пару имя+фамилия с начальными */
  private isTheSameName(name: string): boolean{
    return (name.toLocaleUpperCase() == this.oldName.toLocaleUpperCase());
  }
}
