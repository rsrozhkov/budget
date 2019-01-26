package info.ateh.budgetwebapp.repository;

import info.ateh.budgetwebapp.entity.Member;
import info.ateh.budgetwebapp.entity.Transaction;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Date;

@Configuration
@Slf4j
class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(MemberRepository memberRepository, TransactionRepository transactionRepository) {

        Member vasily = new Member("Василий Чапаев");
        Member petka = new Member("Пётр Исаев");
        Member anka = new Member("Мария Попова");


        return args -> {
            log.info("Preloading " + memberRepository.save(vasily));
            log.info("Preloading " + memberRepository.save(petka));
            log.info("Preloading " + memberRepository.save(anka));
            log.info("Preloading " + memberRepository.save(new Member("vaska")));
            log.info("Preloading " + transactionRepository.save(new Transaction(new Date(119,0,1), vasily, 10000000L, "Положил       на счёт все свои себрежения")));
            log.info("Preloading " + transactionRepository.save(new Transaction(new Date(119,0,2), petka,  5000000L, "Положил на счёт все свои себрежения")));
            log.info("Preloading " + transactionRepository.save(new Transaction(new Date(119,0,3), anka, 3000000L, "Положила на счёт все свои себрежения")));
            log.info("Preloading " + transactionRepository.save(new Transaction(new Date(119,0,15), petka, -500000L, "Купил телеграфный кабель")));
            log.info("Preloading " + transactionRepository.save(new Transaction(new Date(119,0,17), vasily, -3000000L, "Купил коня")));
            log.info("Preloading " + transactionRepository.save(new Transaction(new Date(119,0,19), anka, -1500000L, "Купила пулемёт")));
        };
    }
}
