/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package telproviderme.deposit;

import java.util.Hashtable;
import ua.telnumberident.ruslan.PhoneNumber;
import ua.telnumberident.ruslan.PhoneProvider;

/**
 *
 * @author ruslan
 */
public class PortmoneScript {
    
    public final static int MAX_BUFFER_LEN = 40;
    
    private final static char STEPS_DELIMITER = '*';
    private final static char MOBILE_CHOICE = '1';
    private final static char OTHERNUMBER_CHOICE = '2';
    private final static char COMISSION_AGREE_CHOICE = '1';
    private final static Hashtable operatorsMap = new Hashtable();
    private final static String STEP_HEADER = "135";
    private final static char ENDING = '#';
    private final static Hashtable commissionMap = new Hashtable();
    
    public void PortmoneScript(){
        operatorsMap.put(PhoneProvider.VODAFONE, "1");
        operatorsMap.put(PhoneProvider.KIEVSTAR, "3");
        operatorsMap.put(PhoneProvider.LIFE, "4");
        operatorsMap.put(PhoneProvider.INTERTELEKOM, "5");
        operatorsMap.put(PhoneProvider.UTEL, "6");
        operatorsMap.put(PhoneProvider.PEOPLENET, "7");
        
        commissionMap.put(PhoneProvider.LIFE, "1");
        commissionMap.put(PhoneProvider.INTERTELEKOM, "1");
        commissionMap.put(PhoneProvider.UTEL, "1");
        commissionMap.put(PhoneProvider.PEOPLENET, "1");
    }
    /**
     * 
     * @param operator
     * @param number
     * @param amount
     * @param mobyCode
     * @return 
     */
    public String get(PhoneProvider operator, PhoneNumber number, Integer amount, Integer mobyCode){
        String operatorStr = operator.toString();
        if (!operatorsMap.containsKey(operator.toString())) {
            throw new ProviderNotSupportedException("Provider not supported:" + operatorStr);
        }
        StringBuffer buffer = new StringBuffer(MAX_BUFFER_LEN);
        // add header
        buffer.append(STEPS_DELIMITER);
        buffer.append(STEP_HEADER);
        // add Mobile choice
        buffer.append(STEPS_DELIMITER);
        buffer.append(MOBILE_CHOICE);
        // add provider choice
        buffer.append(STEPS_DELIMITER);
        buffer.append(operatorsMap.get(operatorStr));
        if (hasComission(operatorStr)) {
            buffer.append(STEPS_DELIMITER);
            buffer.append(COMISSION_AGREE_CHOICE);
        }
        // add 'To Other number'
        buffer.append(STEPS_DELIMITER);
        buffer.append(OTHERNUMBER_CHOICE);
        // add Number
        buffer.append(STEPS_DELIMITER);
        buffer.append(getPhoneNumber(number));
        // add amount
        buffer.append(STEPS_DELIMITER);
        buffer.append(amount);
        buffer.append(STEPS_DELIMITER);
        // add mobicode
        buffer.append(STEPS_DELIMITER);
        buffer.append(mobyCode);
        // add ending
        buffer.append(ENDING);

        return buffer.toString();
    }

    public static String getPhoneNumber(PhoneNumber number) {
        String str = number.toString();
        int numDigits = 9;
        return str.substring(str.length() - numDigits, numDigits);
    }

    private boolean hasComission(String operatorStr) {
        return commissionMap.containsKey(operatorStr);
    }
}
