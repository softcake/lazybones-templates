module org.softcake.cucumber.fairy.tale.pigs {
    requires org.softcake.cucumber.actors;
    requires transitive  org.softcake.cucumber.fairy.tale;
    requires org.softcake.cucumber.fairy.tale.formula;

    provides org.softcake.cucumber.fairy.tale.Tale
            with org.softcake.cucumber.fairy.tale.pigs.ThreeLittlePigs;
}
