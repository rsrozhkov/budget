export class Member {
  id: number;
  name: string;

  constructor(name: string) {
    if (name == null || name.trim() == "") {
      let error = "Ошибка. Попытка создания члена семьи с неверными параметрами.";
      alert(error);
      console.log(error);
      return;
    }
    this.name = name.trim();
  }
}
