import { Component, OnInit } from '@angular/core';
import {Member} from "../../classes/member";
import {MemberService} from "../../services/member.service";
import {TransactionService} from "../../services/transaction.service";

@Component({
  selector: 'app-members',
  templateUrl: './members.component.html',
  styleUrls: ['./members.component.css']
})
export class MembersComponent implements OnInit {
  members: Member[];
  memberName;

  constructor(private memberService: MemberService,
              private transactionService: TransactionService) {}

  ngOnInit() {
    this.getMembers();
  }

  /** Получает всех членов семьи с сервера */
  private getMembers(): void {
    this.memberService.getMembers().subscribe(members => this.members = members);
  }

  /** Добавляет нового члена семьи на сервер */
  add(name: string): void {
    this.memberService.isNameTaken(name).subscribe(result =>{
      if (!result)
        this.memberService.addMember(new Member(name))
          .subscribe(member => {
            this.members.push(member)
            this.getMembers();
          });
    });
  }

  /** Удаляет члена семьи с сервера */
  private delete(member: Member): void {
    this.transactionService.checkForOwner(member.id)
      .subscribe(result => {
        if (result) alert(`Удаление не возможго, так как у ${member.name} есть транзакции. Сначала удалите транзакции.`);
        else this.deleteMember(member);
      });
  }

  private deleteMember(member: Member) {
    this.members = this.members.filter(m => m !== member);
    this.memberService.deleteMember(member).subscribe();
  }

}
