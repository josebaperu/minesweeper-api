package com.root.resource;

import com.root.entity.Game;
import com.root.model.Box;
import com.root.repository.GameRepository;
import com.root.service.TableService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/game")
public class GameResource {

    @Inject
    private GameRepository gameRepository;

    @Inject
    private TableService tableService;

    @GET
    @Path("/new/{row}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNewGame(@PathParam("row") final Integer row) {
        final List<Box> listOfBoxes =  tableService.generateNewTableGame(row);
        final Game game = new Game();
        game.setData(listOfBoxes.toString());
        game.setUserId(1);
        gameRepository.save(game);
        Optional<Game>
                g = gameRepository.findById(game.getId());
        return Response
                .status(Response.Status.OK)
                .entity(g.get())
                .build();
    }
    @GET
    @Path("/update/{id}/{row}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateGame(@PathParam("id") final Integer id, @PathParam("row") final Integer row) {
        final Optional<Game>
                g = gameRepository.findById(id);
        final List<Box> listOfBoxes =  tableService.parseExistingTableGameFromString(g.get().getData());
        final List<Box> lbox = tableService.click(row, listOfBoxes);
        g.get().setData(lbox.toString());
        boolean isGameOver = lbox.stream().anyMatch( b -> b.isVisible() && b.isHasBomb());
        if(isGameOver) {
            g.get().setGameOver(true);
        } else {
            long totalOpenBoxes = lbox.stream().filter( b -> b.isVisible() && !b.isHasBomb()).count();
            long totalBombs = lbox.stream().filter(Box::isHasBomb).count();
            if(lbox.size() == totalBombs + totalOpenBoxes) {
                g.get().setYouWon(true);
            }

        }
        gameRepository.save(g.get());
        return Response
                .status(Response.Status.OK)
                .entity(g.get())
                .build();
    }
}
