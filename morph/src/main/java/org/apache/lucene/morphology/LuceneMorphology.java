/**
 * Copyright 2009 Alexander Kuznetsov
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
package org.apache.lucene.morphology;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class LuceneMorphology extends MorphologyImpl {

    public LuceneMorphology(String fileName, LetterDecoderEncoder decoderEncoder) throws IOException {
        super(fileName, decoderEncoder);
    }

    public LuceneMorphology(InputStream inputStream, LetterDecoderEncoder decoderEncoder) throws IOException {
        super(inputStream, decoderEncoder);
    }

    protected void readRules(BufferedReader bufferedReader) throws IOException {
        String s;
        int amount;
        s = bufferedReader.readLine();
        amount = Integer.parseInt(s);
        rules = new Heuristic[amount][];
        for (int i = 0; i < amount; i++) {
            String s1 = bufferedReader.readLine();
            int ruleLenght = Integer.parseInt(s1);
            Heuristic[] heuristics = new Heuristic[ruleLenght];
            for (int j = 0; j < ruleLenght; j++) {
                heuristics[j] = new Heuristic(bufferedReader.readLine());
            }
            rules[i] = modeifyHeuristic(heuristics);
        }
    }


    private Heuristic[] modeifyHeuristic(Heuristic[] heuristics) {
        ArrayList<Heuristic> result = new ArrayList<>();
        for (Heuristic heuristic : heuristics) {
            boolean isAdded = true;
            for (Heuristic ch : result) {
                isAdded = isAdded && !(ch.getActualNormalSuffix().equals(heuristic.getActualNormalSuffix()) && (ch.getActualSuffixLength() == heuristic.getActualSuffixLength()));
            }
            if (isAdded) {
                result.add(heuristic);
            }
        }
        return result.toArray(new Heuristic[0]);
    }

    public boolean checkString(String s) {
        return decoderEncoder.checkString(s);
    }
}
