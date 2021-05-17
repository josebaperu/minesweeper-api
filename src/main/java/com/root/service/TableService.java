package com.root.service;

import com.root.model.Box;

import java.util.List;

public interface TableService {

    List<Box> generateNewTableGame(final int rows);
    List<Box> parseExistingTableGameFromString(final String str);
    List<Box> click(final int boxId, final List<Box> listOfBoxes);
}
