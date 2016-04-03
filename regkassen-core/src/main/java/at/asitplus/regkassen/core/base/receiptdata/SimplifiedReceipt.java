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

/**
 * Simple representation of a receipt, only the summed up TAX-SET values are modeled
 */
public class SimplifiedReceipt {
    protected long taxSetNormal;
    protected long taxSetErmaessigt1;
    protected long taxSetErmaessigt2;
    protected long taxSetNull;
    protected long taxSetBesonders;

    public long getTaxSetNormal() {
        return taxSetNormal;
    }

    public void setTaxSetNormal(long taxSetNormal) {
        this.taxSetNormal = taxSetNormal;
    }

    public long getTaxSetErmaessigt1() {
        return taxSetErmaessigt1;
    }

    public void setTaxSetErmaessigt1(long taxSetErmaessigt1) {
        this.taxSetErmaessigt1 = taxSetErmaessigt1;
    }

    public long getTaxSetErmaessigt2() {
        return taxSetErmaessigt2;
    }

    public void setTaxSetErmaessigt2(long taxSetErmaessigt2) {
        this.taxSetErmaessigt2 = taxSetErmaessigt2;
    }

    public long getTaxSetNull() {
        return taxSetNull;
    }

    public void setTaxSetNull(long taxSetNull) {
        this.taxSetNull = taxSetNull;
    }

    public long getTaxSetBesonders() {
        return taxSetBesonders;
    }

    public void setTaxSetBesonders(long taxSetBesonders) {
        this.taxSetBesonders = taxSetBesonders;
    }
}
