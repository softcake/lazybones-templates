/*
 *
 * Copyright 2018 softcake.org.
 *
 * Licensed under the Apache License, Version 2.0 (the "License").
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.softcake.cucumber.fairy.tale.bears;

import org.softcake.cucumber.actors.api.Actor;
import org.softcake.cucumber.actors.api.Imagination;
import org.softcake.cucumber.fairy.tale.Tale;
import org.softcake.cucumber.fairy.tale.formula.FairyTale;

/**
 * .
 *
 * @author The softcake Authors.
 */
public class GoldilocksAndTheThreeBears implements Tale {
    private Tale fairyTale;

    @Override
    public void tell() {
        if (fairyTale == null) {
            Actor mamaBear = Imagination.createActor("Mama Bear");
            Actor papaBear = Imagination.createActor("Papa Bear");
            Actor babyBear = Imagination.createActor("Baby Bear");
            Actor bears = Imagination.createGroup("bears", mamaBear, papaBear, babyBear);
            Actor goldie = Imagination.createActor("Goldilocks");
            fairyTale = FairyTale.getWeaver()
                                 .record(goldie, "was walking in the woods, when she "
                                                 + "stumbled upon the house of", bears)
                                 .record(goldie, "didn't know it was the house of", bears)
                                 .record(goldie, "went inside and looked at the breakfast "
                                                 + "on the table.")
                                 .record(goldie, "tried the porridge of", papaBear)
                                 .record(goldie, "said, 'That is too hot!'")
                                 .record(goldie, "tried the porridge of", mamaBear)
                                 .record(goldie, "said, 'That is too cold!'")
                                 .record(goldie, "tried the porridge of", babyBear)
                                 .record(goldie, "said, 'That is just right!' and ate it "
                                                 + "all up.")
                                 .record(goldie, "went up to try out the beds of", bears)
                                 .record(goldie, "laid down in the bed of", papaBear)
                                 .record(goldie, "said, 'That is too hard!'")
                                 .record(goldie, "laid down in the bed of", mamaBear)
                                 .record(goldie, "said, 'That is too soft!'")
                                 .record(goldie, "laid down in the bed of", babyBear)
                                 .record(goldie,
                                         "said, 'That is just right!', and fell fast asleep.")
                                 .record(bears, "came home.")
                                 .record(bears, "went into the kitchen.")
                                 .record(papaBear, "said, 'Somebody has been eating my "
                                                   + "porridge.'")
                                 .record(mamaBear, "said, 'Somebody has been eating my "
                                                   + "porridge.'")
                                 .record(babyBear, "said, 'Somebody has been eating my "
                                                   + "porridge. And they ate it all up.'")
                                 .record(bears, "went upstairs.")
                                 .record(papaBear, "said, 'Somebody has been sleeping in "
                                                   + "my bed.'")
                                 .record(mamaBear, "said, 'Somebody has been sleeping in "
                                                   + "my bed.'")
                                 .record(babyBear, "said, 'Somebody has been sleeping in "
                                                   + "my bead. And she is still there!'.")
                                 .record(bears, "scared", goldie)
                                 .record(goldie, "ran out of the house at top speed to "
                                                 + "escape", bears).weave();
        }
        fairyTale.tell();
    }
}
