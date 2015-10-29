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

package at.asitplus.regkassen.core.modules.signature.jws;

import at.asitplus.regkassen.core.base.rksuite.RKSuite;
import at.asitplus.regkassen.core.base.util.CashBoxUtils;
import org.jose4j.jws.EcdsaUsingShaAlgorithm;

import java.io.IOException;
import java.security.*;

/**
 * Manual JWS signature module, that does not require any JWS library
 * For Reg-Kassen Verordnung, we just need one simple method
 * 1. concatenated BASE64-URL encoding of HEADER and PAYLOAD (via ".")
 * 2. This is the data to be signed, which is hashed via SHA-256
 * 3. The hash is then signed via ECDSA
 * 4. Only additional task to do due to JWS spec: encode signature result according to JWS spec (//TODO replace method from JWS lib here with own method)
 */
public class ManualJWSModule extends AbstractJWSModule {

    @Override
    public String signMachineCodeRepOfReceipt(String machineCodeRepOfReceipt, RKSuite rkSuite) {
        try {

            //prepare data to be signed, "ES256 JWS header" fixed (currently the only relevant signature/hash method (RK1)
            String jwsHeaderBase64Url = "eyJhbGciOiJFUzI1NiJ9";
            String jwsPayloadBase64Url = CashBoxUtils.base64Encode(machineCodeRepOfReceipt.getBytes(), true);
            String jwsDataToBeSigned = jwsHeaderBase64Url + "." + jwsPayloadBase64Url;

            //prepare signature according to JAVA JCE/JCA
            Signature signature = Signature.getInstance("SHA256withECDSA");
            signature.initSign(signatureModule.getSigningKey());
            signature.update(jwsDataToBeSigned.getBytes());

            //sign data
            byte[] signatureResult = signature.sign();

            //encode according to JWS spec //TODO replace with own method
            byte[] jwsSignature = EcdsaUsingShaAlgorithm.convertDerToConcatenated(signatureResult, 64);

            //encode result as BASE64-URL
            String jwsSignatureBase64Url = CashBoxUtils.base64Encode(jwsSignature, true);

            //store as JWS compact representation
            return jwsHeaderBase64Url + "." + jwsPayloadBase64Url + "." + jwsSignatureBase64Url;

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}