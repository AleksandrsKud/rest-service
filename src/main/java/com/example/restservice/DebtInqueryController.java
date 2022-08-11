package com.example.restservice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class DebtInqueryController {

   private final Integer maxAmount = 10000;
   private final Integer maxPeriod = 60;

   private final Integer minAmount = 2000;
   private final Integer minPeriod = 12;

   private final Integer step = 1;
    public Person FindPersonByPersCode(
            String personalCode, List<Person> persons) {

        for (Person person : persons) {
            if (person.getPersonalCode().equals(personalCode)) {
                return person;
            }
        }
        return null;
    }

    public void setInquery(DebtInquery debtInquery, String decision, long amount, Integer period, double score, String message){
        debtInquery.setDecision(decision);
        debtInquery.setAmount(amount);
        debtInquery.setPeriod(period);
        debtInquery.setScore(score);
        debtInquery.setMessage(message);
    }

    public double calculateScore(Integer modifier, long amount, Integer period){

        double score = (double) modifier / amount * period;
        System.out.println("Mod: " + modifier + "; amount:" + amount + "; period:" + period + "; score:" + score);
        return score;
    }

    @GetMapping("/debtInquery")
    @CrossOrigin
    public DebtInquery debtInquery(@RequestParam(value = "personalCode") String personalCode, @RequestParam(value = "amount") long amount, @RequestParam(value = "period") Integer period){

        long outputAmount = amount;
        Integer outputPeriod = period;

        //Constraints checking
        if (outputPeriod < minPeriod){ //min-max requirement
            outputPeriod = minPeriod;
        }else if(outputPeriod > maxPeriod){
            outputPeriod = maxPeriod;
        }

        //Constraints checking
        if (outputAmount < minAmount){ //min-max requirement
            outputAmount = minAmount;
        }else if (outputAmount > maxAmount){
            outputAmount = maxAmount;
        }

        DebtInquery debtInquery = new DebtInquery(personalCode, null, outputAmount, outputPeriod, 0, "");
        // mocking customers/persons
        List<Person> persons = new ArrayList<>();
        persons.add(new Person("49002010965", 0));
        persons.add(new Person("49002010976", 100));
        persons.add(new Person("49002010987", 300));
        persons.add(new Person("49002010998", 1000));

        try {
            Person person = FindPersonByPersCode(personalCode, persons);

            if (person == null)
                throw new Exception("Person not found");

            if (person.getModifier() == 0) { //0 modifier means person have debts
                setInquery(debtInquery, "Negative", 0,0,0, Messages.format("HAS_DEBTS", person.getPersonalCode()));
            } else {
                //check score with provided variables
                double score = calculateScore(person.getModifier(), outputAmount, outputPeriod);

                //if score > 1, then we can increase amount
                if (score > 1 ){
                    for (long i = outputAmount; i < maxAmount; i +=step){
                        outputAmount += step;
                        score = calculateScore(person.getModifier(), outputAmount, outputPeriod);
                        if (score <=1.01){
                            break;
                        }
                    }
                    setInquery(debtInquery, "Positive", outputAmount,outputPeriod, score, Messages.format("AMOUNT_INCREASED", person.getPersonalCode()));
                }else if (score < 1){ //is score < 1, we need either to increase period or de-crease amount.
                    for (int j = outputPeriod; j < maxPeriod; j++){
                        outputPeriod += + 1;
                        score = calculateScore(person.getModifier(), outputAmount, outputPeriod);
                        if (score >= 1){
                            break;
                        }
                    }
                    if (score >= 1){ //increasing period helped to get a positive score.
                        setInquery(debtInquery, "Positive", outputAmount,outputPeriod, score, Messages.format("PERIOD_INCREASED", person.getPersonalCode()));
                    }else{  //increasing period did not help. De-creasing amount leaving max allowed period
                        for (long k = outputAmount; k >= minAmount; k -=step){
                            outputAmount -=step;
                            score = calculateScore(person.getModifier(), outputAmount, outputPeriod);
                            if (score >= 1){
                                break;
                            }
                        }
                        if (score >= 1){
                            setInquery(debtInquery, "Positive", outputAmount,outputPeriod, score, Messages.format("PERIOD_INC_AMT_DEC", person.getPersonalCode()));
                        }else{
                            setInquery(debtInquery, "Negative", outputAmount,outputPeriod, score, Messages.format("SCORE_NEGATIVE", person.getPersonalCode()));
                        }
                    }
                }else{  //score meets requirements with a provided variables
                    setInquery(debtInquery, "Positive", outputAmount,outputPeriod, score, Messages.format("SCORE_POSITIVE", person.getPersonalCode()));
                }
            }
        }
        catch (Exception e){
            throw new Error(e.getMessage());
        }

        return debtInquery;
    }
}
