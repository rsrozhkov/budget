import {Member} from "./member";

export class Transaction {
  id: number;
  date: Date;
  member: Member;
  amount: number;
  comment: string;

  constructor(member: Member, amount: number, comment: string) {
    if (member == null || amount == null || comment == null || amount == 0 || comment.trim() == "") {
      let error = "Ошибка. Попытка создания транзакции с неверными параметрами.";
      alert(error);
      console.log(error);
      return;
    }
    this.date = new Date();
    this.member = member;
    this.amount = amount;
    this.setComment(comment);
  }

  /**
   * Убирает лишние пробелы из строки
   */
  private setComment(comment: string) {
    this.comment = comment.replace(/\s+/g, ' ').trim();
  }
}
