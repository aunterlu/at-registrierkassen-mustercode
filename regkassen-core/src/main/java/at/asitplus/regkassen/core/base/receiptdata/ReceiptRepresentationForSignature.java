/*
 * Copyright (C) 2015
 * A-SIT Plus GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package at.asitplus.regkassen.core.base.receiptdata;

import at.asitplus.regkassen.core.base.rksuite.RKSuite;
import at.asitplus.regkassen.core.base.util.CashBoxUtils;
import com.google.gson.annotations.SerializedName;
import org.apache.commons.math3.util.Precision;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * This class represents the data structure that is specified in Detailspezifikation Abs 4
 * For signature generation the data is prepared according to Detailspezifikation Abs 5
 */
public class ReceiptRepresentationForSignature {
    //REF TO SPECIFICATION: Detailspezifikation/Abs 4, Abs 5

    @SerializedName("Kassen-ID")
    protected String cashBoxID;

    @SerializedName("Belegnummer")
    protected String receiptIdentifier;

    @SerializedName("Beleg-Datum-Uhrzeit")
    protected Date receiptDateAndTime;

    @SerializedName("Betrag-Satz-Normal")
    protected long sumTaxSetNormal;

    @SerializedName("Betrag-Satz-Ermaessigt-1")
    protected long sumTaxSetErmaessigt1;

    @SerializedName("Betrag-Satz-Ermaessigt-2")
    protected long sumTaxSetErmaessigt2;

    @SerializedName("Betrag-Satz-Null")
    protected long sumTaxSetNull;

    @SerializedName("Betrag-Satz-Besonders")
    protected long sumTaxSetBesonders;

    @SerializedName("Stand-Umsatz-Zaehler-AES256-ICM")
    protected String encryptedTurnoverValue;

    @SerializedName("Zertifikat-Seriennummer")
    protected String signatureCertificateSerialNumber;

    @SerializedName("Sig-Voriger-Beleg")
    protected String signatureValuePreviousReceipt;

    private String cent2euroFormat(long cent) {
       StringBuffer euro = new StringBuffer(4);
       if (cent < 0) {
          euro.append("-");
          cent = cent * -1;
       }
       String c = String.valueOf(cent);
       if (c.length() == 1) {
          euro.append("0,0");
          euro.append(c);
       } else if (c.length() == 2) {
          euro.append("0,");
          euro.append(c);
       } else {
          euro.append(c.substring(0, c.length() - 2));
          euro.append(",");
          euro.append(c.substring(c.length() - 2, c.length()));
       }
       return euro.toString();
    }
    
    /**
     * return a decimal format of "-XXX,XX" euro for the given cent value
     * @param cent
     * @return euro formatted string for the given cent value
     */
    private String cent2euroFormat(long cent) {
       StringBuffer euro = new StringBuffer(4);
       if (cent < 0) {
          euro.append("-");
          cent = cent * -1;
       }
       String c = String.valueOf(cent);
       if (c.length() == 1) {
          euro.append("0,0");
          euro.append(c);
       } else if (c.length() == 2) {
          euro.append("0,");
          euro.append(c);
       } else {
          euro.append(c.substring(0, c.length() - 2));
          euro.append(",");
          euro.append(c.substring(c.length() - 2, c.length()));
       }
       return euro.toString();
    }
    
    /**
     * get plain data for signature generation according to Detailspezifikation Abs 5
     *
     * @param rkSuite RK suite according to Detailspezifikation Abs 2
     * @return data-to-be-signed result of algorithm in Detailspezifikation/Abs 5
     */
    public String getDataToBeSigned(RKSuite rkSuite) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        //prepare signature payload string for signature creation (Detailspezifikation/ABS 5
        return "_" + rkSuite.getSuiteID() + "_" + cashBoxID + "_" + receiptIdentifier + "_" + dateFormat.format(receiptDateAndTime) 
              + "_" + cent2euroFormat(sumTaxSetNormal) + "_" + cent2euroFormat(sumTaxSetErmaessigt1) + "_" + cent2euroFormat(sumTaxSetErmaessigt2) 
              + "_" + cent2euroFormat(sumTaxSetNull) + "_" + cent2euroFormat(sumTaxSetBesonders) 
              + "_" + encryptedTurnoverValue + "_" + signatureCertificateSerialNumber + "_" + signatureValuePreviousReceipt;
    }

    /**
     * get first part of OCR code representation (without the signature value)
     *
     * @param rkSuite RK suite according to Detailspezifikation Abs 2
     * @return first part of OCR code representation (without the signature value)
     * difference to getDataToBeSigned: BASE64 values are re-encoded to BASE32 to simplify OCR process
     */
    public String getOCRCodeRepresentationWithoutSignature(RKSuite rkSuite) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        //prepare signature payload string for signature creation (Detailspezifikation/ABS 5
        String base32RepOfSignatureValuePreviousReceipt = CashBoxUtils.base32Encode(CashBoxUtils.base64Decode(signatureValuePreviousReceipt, false));
        String base32EncryptedTurnoverValue = CashBoxUtils.base32Encode(CashBoxUtils.base64Decode(encryptedTurnoverValue, false));

        return "_" + rkSuite.getSuiteID() + "_" + cashBoxID + "_" + receiptIdentifier + "_" + dateFormat.format(receiptDateAndTime)
              + "_" + cent2euroFormat(sumTaxSetNormal) + "_" + cent2euroFormat(sumTaxSetErmaessigt1) + "_" + cent2euroFormat(sumTaxSetErmaessigt2)
                    + "_" + cent2euroFormat(sumTaxSetNull) + "_" + cent2euroFormat(sumTaxSetBesonders) 
                    + "_" + base32EncryptedTurnoverValue + "_" + signatureCertificateSerialNumber + "_" + base32RepOfSignatureValuePreviousReceipt;
    }

    public String getCashBoxID() {
        return cashBoxID;
    }

    public void setCashBoxID(String cashBoxID) {
        this.cashBoxID = cashBoxID;
    }

    public String getReceiptIdentifier() {
        return receiptIdentifier;
    }

    public void setReceiptIdentifier(String receiptIdentifier) {
        this.receiptIdentifier = receiptIdentifier;
    }

    public Date getReceiptDateAndTime() {
        return receiptDateAndTime;
    }

    public void setReceiptDateAndTime(Date receiptDateAndTime) {
        this.receiptDateAndTime = receiptDateAndTime;
    }

    public long getSumTaxSetNormal() {
        return sumTaxSetNormal;
    }

    public void setSumTaxSetNormal(long sumTaxSetNormal) {
        this.sumTaxSetNormal = sumTaxSetNormal;
    }

    public long getSumTaxSetErmaessigt1() {
        return sumTaxSetErmaessigt1;
    }

    public void setSumTaxSetErmaessigt1(long sumTaxSetErmaessigt1) {
        this.sumTaxSetErmaessigt1 = sumTaxSetErmaessigt1;
    }

    public long getSumTaxSetErmaessigt2() {
        return sumTaxSetErmaessigt2;
    }

    public void setSumTaxSetErmaessigt2(long sumTaxSetErmaessigt2) {
        this.sumTaxSetErmaessigt2 = sumTaxSetErmaessigt2;
    }

    public long getSumTaxSetNull() {
        return sumTaxSetNull;
    }

    public void setSumTaxSetNull(long sumTaxSetNull) {
        this.sumTaxSetNull = sumTaxSetNull;
    }

    public long getSumTaxSetBesonders() {
        return sumTaxSetBesonders;
    }

    public void setSumTaxSetBesonders(long sumTaxSetBesonders) {
        this.sumTaxSetBesonders = sumTaxSetBesonders;
    }

    public String getEncryptedTurnoverValue() {
        return encryptedTurnoverValue;
    }

    public void setEncryptedTurnoverValue(String encryptedTurnoverValue) {
        this.encryptedTurnoverValue = encryptedTurnoverValue;
    }

    public String getSignatureCertificateSerialNumber() {
        return signatureCertificateSerialNumber;
    }

    public void setSignatureCertificateSerialNumber(String signatureCertificateSerialNumber) {
        this.signatureCertificateSerialNumber = signatureCertificateSerialNumber;
    }

    public String getSignatureValuePreviousReceipt() {
        return signatureValuePreviousReceipt;
    }

    public void setSignatureValuePreviousReceipt(String signatureValuePreviousReceipt) {
        this.signatureValuePreviousReceipt = signatureValuePreviousReceipt;
    }
}
