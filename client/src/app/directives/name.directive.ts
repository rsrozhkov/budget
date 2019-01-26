import {Directive, forwardRef, Injectable} from "@angular/core";
import {AbstractControl, AsyncValidator, NG_ASYNC_VALIDATORS, ValidationErrors} from "@angular/forms";
import {Observable} from "rxjs";
import {catchError, map} from "rxjs/operators";
import {MemberService} from "../services/member.service";

@Injectable({ providedIn: 'root' })
export class UniqueNameValidator implements AsyncValidator {
  constructor(private memberService: MemberService) {}

  validate(ctrl: AbstractControl):
    Promise <ValidationErrors | null> | Observable<ValidationErrors | null> {
    return this.memberService.isNameTaken(ctrl.value).pipe(
      map(isTaken => (isTaken ? { uniqueName: true } : null)),
      catchError(() => null)
    );
  }
}

@Directive({
  selector: '[appUniqueName]',
  providers: [
    {
      provide: NG_ASYNC_VALIDATORS,
      useExisting: forwardRef(() => UniqueNameValidator),
      multi: true
    }
  ]
})

export class UniqueNameValidatorDirective {
  constructor(private validator: UniqueNameValidator) {}

  validate(control: AbstractControl) {
    this.validator.validate(control);
  }
}
