/*
 * Copyright (C) 2022 DANS - Data Archiving and Networked Services (info@dans.knaw.nl)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package nl.knaw.dans.ingest.core.service.exception;

import nl.knaw.dans.ingest.core.service.Deposit;

public class FailedDepositException extends RuntimeException {
    public FailedDepositException(Deposit deposit, String message) {
        super(String.format("Failed %s: %s", deposit.getDir(), message));
    }

    public FailedDepositException(Deposit deposit, String message, Throwable e) {
        super(String.format("Failed %s: %s", deposit.getDir(), message), e);
    }
}
