import {Component, OnInit} from '@angular/core';
import {TransactionService} from "../../services/transaction.service";
import {TransactionType} from "../../classes/transaction-type";
import {Transaction} from "../../classes/transaction";
import {Member} from "../../classes/member";
import {MemberService} from "../../services/member.service";

@Component({
  selector: 'app-budget',
  templateUrl: './budget.component.html',
  styleUrls: ['./budget.component.css']
})
export class BudgetComponent implements OnInit {
  balance: number;
  transactionType: TransactionType;
  private selectedMember: Member;
  private members: Member [];
  private notEnoughMoney: boolean;

  constructor(private transactionService: TransactionService,
              private memberService: MemberService) {this.notEnoughMoney = false}

  ngOnInit() {
    this.getBalance();
    this.getMembersSortedByName();
    this.resetTransactionType();
  }

  /** Получает текущий баланс с сервера */
  private getBalance(): void {
    this.transactionService.getBalance().subscribe(balance => {this.balance = balance;});
  }

  /** Получает и сортирует по алфавиту список членов семьи с сервера */
  private getMembersSortedByName () {
    this.memberService.getMembers()
      .subscribe(members => this.members = members
        .sort((prev, next) => {
          if ( prev.name < next.name ) return -1;
          if ( prev.name > next.name ) return 1;
        })
      );
  }

  /** Отмена действий с формой. Обновляет баланс, сбрасывает значения переменныых */
  private goBack(): void {
    this.getBalance();
    this.resetVariables();
    this.notEnoughMoney = false;
  }

  /** Устанавливает неопределенное значение переменной "TransactionType" */
  private resetTransactionType(): void {
    this.setTransactionType(TransactionType.UNDEF)
  }

  /** Сбрасывает значения переменных */
  private resetVariables(): void {
    this.selectedMember = null;
    this.resetTransactionType();
  }

  /** Устанавливает тип транзакции */
  setTransactionType(type: TransactionType) {
    this.transactionType = type;
  }

  /** Отправка формы добавления транзакции на сервер */
  private submit(member: Member, amount: number, comment: string): void {
    if (!amount || amount <= 0) {
      let error = "Ошибка. Неверная сумма транзакции.";
      alert(error);
      console.log(error);
      return;
    }
    amount = Math.abs(amount)*this.transactionType*100;

    this.transactionService.getBalance().subscribe(balance => {
      this.balance = balance;
      if (balance+amount<0) {
        this.notEnoughMoney = true;
        return;
      } else {
        this.notEnoughMoney = false;
        let newTransaction = new Transaction(member, amount, comment);
        this.transactionService
          .addTransaction(newTransaction as Transaction)
          .subscribe(() => this.goBack());
      }
    });
  }

}
