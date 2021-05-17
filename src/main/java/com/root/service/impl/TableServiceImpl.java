package com.root.service.impl;

import com.google.gson.Gson;
import com.root.model.Box;
import com.root.service.TableService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class TableServiceImpl implements TableService {
    final static int[] indexesToParse = new int[] {-100,-99,-101,-1,1,100,101,109};

    @Inject
    private Gson gson;

    @Override
    public List<Box>  generateNewTableGame(final int rows) {
        return new ArrayList<>(generateTable(rows).values());
    }

    @Override
    public List<Box> parseExistingTableGameFromString(String str) {
        final Box[] arrayofBoxes = gson.fromJson(str, Box[].class);

        return Arrays.asList(arrayofBoxes);
    }
    @Override
    public List<Box> click(final int boxId, final List<Box> listOfBoxes) {
        List<Box> newList = new ArrayList<>();
        Box boxFound = listOfBoxes.stream()
                .filter(box -> boxId == box.getId())
                .findAny()
                .orElse(null);
        boxFound.setVisible(true);
        if(boxFound.isHasBomb()){
            newList = listOfBoxes;
        } else{
            List<Integer> recursiveList = new ArrayList<>();
            recursiveList.add(boxId);
            newList = recurseIds(recursiveList,listOfBoxes, 1);
        }
        return newList;
    }
    private synchronized List<Box> recurseIds(List<Integer> ids, List<Box> listOfBoxes, int attempt) {
        if (ids.size() > 0) {
            Iterator<Integer> it = ids.iterator();
            List<Integer> intes = new ArrayList<>();
            while (it.hasNext()) {
                Integer id =  it.next();
                for(int idx : indexesToParse) {
                    final Box box = listOfBoxes.stream()
                            .filter(b -> id+idx == b.getId())
                            .findAny()
                            .orElse(null);
                    if(box != null && !box.isVisible() && box.getBombsAround() >= 0  && attempt == 1){
                        box.setVisible(true);
                        intes.add(id+idx);
                    } else if(box != null && !box.isVisible() && box.getBombsAround() == 0 && attempt > 1) {
                        box.setVisible(true);
                        intes.add(id+idx);
                    }
                }
            }
            recurseIds(intes,listOfBoxes, ++attempt);
        }
        return listOfBoxes;
    }

    private Map<Integer,Box> generateTable(final int boxesRow) {
        final Map<Integer,Box> mapOfBoxes = new LinkedHashMap<>();

        final Random random = new Random();

        for (int i = 1; i < (boxesRow) + 1; i++) {
            for (int j = 1; j < boxesRow + 1; j++) {
                final int id = (i*100) + j;
                final Box box = new Box(id);
                if (random.nextInt(boxesRow) > boxesRow * 0.65) {
                    box.setHasBomb(true);
                    box.setBombsAround(-1);
                }
                mapOfBoxes.put(id,box);
            }
        }
        final List<Box> boxesWithBombs = mapOfBoxes.values().stream().filter(Box::isHasBomb).collect(Collectors.toList());
        final List<Integer> idsWithNoBombs = mapOfBoxes.values().stream().filter(b -> !b.isHasBomb()).map(Box::getId).collect(Collectors.toList());
        for (Box bombBox : boxesWithBombs) {
            final int bombId = bombBox.getId();

            for(int idx : indexesToParse) {
                if (idsWithNoBombs.contains(bombId + idx)) {
                    final Optional<Box> bx = mapOfBoxes.values().stream().filter(b -> b.getId() == bombBox.getId() + idx).findFirst();
                    bx.get().setBombsAround(bx.get().getBombsAround() + 1);
                }
            }
        }
        return mapOfBoxes;
    }
}
