package com.harleylizard.space.graphics.text;

public sealed interface Font permits English {

    FontCoordinates getCoordinates(char c);
}
