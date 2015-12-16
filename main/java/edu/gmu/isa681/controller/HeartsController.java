/**
 * http://websystique.com/spring-security/spring-security-4-password-encoder-bcrypt-example-with-hibernate/
 * http://websystique.com/spring-security/spring-security-4-custom-login-form-annotation-example/
 * http://websystique.com/springmvc/spring-4-mvc-helloworld-tutorial-full-example/
 http://www.mkyong.com/spring-mvc/spring-4-mvc-ajax-hello-world-example/


 * why to use hibernate?
 * http://software-security.sans.org/developer-how-to/fix-sql-injection-in-java-hibernate



 http://crunchify.com/simplest-spring-mvc-hello-world-example-tutorial-spring-model-view-controller-tips/
 http://crunchify.com/simplest-spring-mvc-hello-world-example-tutorial-spring-model-view-controller-tips/
 https://spring.io/guides/gs/securing-web/#use-sts
 */

package edu.gmu.isa681.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.gmu.isa681.model.Player;
import edu.gmu.isa681.model.UnregisteredPlayer;
import edu.gmu.isa681.model.GameBoard;
import edu.gmu.isa681.dto.GameDto;
import edu.gmu.isa681.dto.GameMoveDto;
import edu.gmu.isa681.dto.PlayerGamesDto;
import edu.gmu.isa681.service.GameService;
import edu.gmu.isa681.service.PlayerService;
import edu.gmu.isa681.service.UnregisteredPlayerService;

@Controller
public class HeartsController {

	@Autowired
	PlayerService playerService;

	@Autowired
	GameService gameService;
	
	@Autowired
	UnregisteredPlayerService unregisteredPlayerService;
	
    @Autowired
    @Qualifier("authenticationManager")
    protected AuthenticationManager authenticationManager;
	
    @RequestMapping(value = { "/", "/welcome" }, method = RequestMethod.GET)
    public String sayHello(ModelMap model) {
        return "welcome";
    }
    
    @RequestMapping(value = "/Access_Denied", method = RequestMethod.GET)
    public String sayStop(ModelMap model) {
        return "accessdenied";
    }
    
 
    /*
     * 13-Nov-2015 17:39:05.211 WARNING [http-nio-8010-exec-2] org.springframework.web.servlet.PageNotFound.handleHttpRequestMethodNotSupported Request method 'POST' not supported
13-Nov-2015 17:39:05.215 WARNING [http-nio-8010-exec-2] org.springframework.web.servlet.mvc.support.DefaultHandlerExceptionResolver.logException Handler execution resulted in exception: Request method 'POST' not supported

, method = RequestMethod.POST)
    public @ResponseBody
     */



    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage() {
        return "login";
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String goHome(ModelMap model) {
    	int playerId = LoggedInPlayer.getLoggedInPlayerId();
        
    	PlayerGamesDto playerGamesDto = gameService.getPlayerGamesResults(playerId);
        model.addAttribute("loggedInPlayerName", LoggedInPlayer.getLoggedInPlayerName());
        if(playerGamesDto != null) {
	        model.addAttribute("playerWins", playerGamesDto.getTotalWins());
	        model.addAttribute("playerLosses", playerGamesDto.getTotalLosses());
	        model.addAttribute("playersScores", playerGamesDto.getGames());
        }
        
        return "home";
    }
    
    
    @RequestMapping(value="/logout", method = RequestMethod.POST)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String newRegistration(ModelMap model) {
    	UnregisteredPlayer uPlayer = new UnregisteredPlayer();
        model.addAttribute("player", uPlayer);
        return "register";
    }
    
    /*
     * This method will be called on form submission, handling POST request It
     * also validates the player input
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String saveRegistration(@Valid UnregisteredPlayer uPlayer,
            BindingResult result, ModelMap model, HttpServletRequest request, HttpServletResponse response) {

        if (result.hasErrors()) {
            System.out.println("There are errors");
            return "register";
        }
        
        Player player = new Player();
        String errMsg = unregisteredPlayerService.checkPlayer(uPlayer);
        
        if (!errMsg.isEmpty()) {
            model.addAttribute("failure", errMsg);
            model.addAttribute("playerError", "Y");
            model.addAttribute("player", uPlayer);
        	return "register";
        }
        
        player.setFirstName(uPlayer.getFirstName());
        player.setLastName(uPlayer.getLastName());
        player.setSsoId(uPlayer.getSsoId());
        player.setPassword(uPlayer.getPassword());
        player.setEmail(uPlayer.getEmail());

        playerService.save(player);

//        System.out.println("First Name : "+player.getFirstName());
//        System.out.println("Last Name : "+player.getLastName());
//        System.out.println("SSO ID : "+player.getSsoId());
//        System.out.println("Password : "+player.getPassword());
//        System.out.println("Email : "+player.getEmail());
//        System.out.println("Checking UsrProfiles....");

        model.addAttribute("success", "Player " + player.getSsoId() + " has been registered successfully.");
        model.addAttribute("newPlayer", "Y");
        
      //After successfully Creating user
        authenticateUserAndSetSession(player, request);

        return "home";
    }
    
    @RequestMapping(value="/joingame", method = RequestMethod.POST)
    public String joinGame (HttpServletRequest request, HttpServletResponse response) {
    	int playerId = LoggedInPlayer.getLoggedInPlayerId();
    	
    	// 1. a) Check if the player already in a game and the game is not over, then return the game Id.
    	//    b) Else check if there is an open game then have him join the game and return the game Id.
    	
    	gameService.joinAGame(playerId);    	
    	
        return "redirect:/board";
    }
    
    
    @RequestMapping(value="/trashCards", method = RequestMethod.POST)
    public String trashCards (@Valid GameBoard gameBoard, 
	           BindingResult result, ModelMap model) {
    	
        if (result.hasErrors()) {
            System.out.println("There are errors");
            return "board";
        }
    	
    	int playerId = LoggedInPlayer.getLoggedInPlayerId();
    	int gameId = gameBoard.getGameId();
    	String[] trashCards = gameBoard.getTrashCards();

    	System.out.println("_______________________________ GameID=" + gameId);
    	System.out.println("_______________________________ cardId=" + trashCards[0]);
    	
    	if(trashCards.length != 3) {
    		// display error and do not continue.
			String cheat = LoggedInPlayer.getLoggedInPlayerSso() + " tried not to pass only 3 cards.";
			gameService.setCheaterMsg(playerId, gameBoard.getGameId(), cheat);
	    	return board(-1, model);
    	}    	
    	
    	List<String> playerCards = gameService.getPlayerCards(playerId, gameId);
    	int found = 0;
    	
    	//loop through the list to determine if the player has the cards
    	boolean isFirstCardValid = false;
    	boolean isSecondCardValid = false;
    	boolean isThirdCardValid = false;
		for (int i = 0; i < playerCards.size(); i++) {
			if (playerCards.get(i).equals(trashCards[0]) || playerCards.get(i).equals(trashCards[1]) || playerCards.get(i).equals(trashCards[2])) {				
				found+=1;
				if(playerCards.get(i).equals(trashCards[0])) isFirstCardValid = true;
				if(playerCards.get(i).equals(trashCards[1])) isSecondCardValid = true;
				if(playerCards.get(i).equals(trashCards[2])) isThirdCardValid = true;
				
		    	System.out.println("_______________________________ found=" + found);
				
				
				continue;
			}
		}
		if (found!=3) {
	    	//player tried to pass a card that he does not have.	    	
			//notify users and prevent card from being played.
			String cardsNotWithPlayers = "[";
			if(!isFirstCardValid) cardsNotWithPlayers = cardsNotWithPlayers.concat(trashCards[0]).concat(" ");
			if(!isSecondCardValid) cardsNotWithPlayers = cardsNotWithPlayers.concat(trashCards[1]).concat(" ");
			if(!isThirdCardValid) cardsNotWithPlayers = cardsNotWithPlayers.concat(trashCards[2]).concat(" ");
			cardsNotWithPlayers = cardsNotWithPlayers + "]";
			String cheat = LoggedInPlayer.getLoggedInPlayerSso() + " did not pass a valid card from their hand. Player does not have these cards: " + cardsNotWithPlayers;
			gameService.setCheaterMsg(playerId, gameBoard.getGameId(), cheat);
	    	return board(-1, model);
		} else {
			gameService.trashCards(gameId, playerId, trashCards);
		}		
    	
        return "redirect:/board";
    }    
    
    //, @RequestParam(value="oldGameId") Integer oldGameId
    @RequestMapping(value="/board", method = RequestMethod.GET)
    public String board(@RequestParam(value = "oldGameId", defaultValue = "-1", required = false) final Integer oldGameId, ModelMap model) {
    	int oldGame = 0;
    	int playerId = LoggedInPlayer.getLoggedInPlayerId();
    	GameDto game = null;
    	
    	// 1. a) Check if the player already in a game and the game is not over, then return the game Id.
    	//    b) Else check if there is an open game then have him join the game and return the game Id.
    	
    	if (oldGameId == -1) {
    		game = gameService.getGame(playerId);
    	} else if (oldGameId > 0) {
    		game = gameService.getGameHistory(oldGameId,playerId);
    		oldGame = 1;
    	} else {
			model.addAttribute("failure", "Invalid game requested.");
			model.addAttribute("playerError", "Y");
			return goHome(model);
    	}
    	
		if (game == null) {
			model.addAttribute("failure", "Requested game not found.");
			model.addAttribute("playerError", "Y");
			return goHome(model);
		}
    	
    	System.out.println("++++++++++++++++++++++++" + game.getPlayerId());
        model.addAttribute("loggedInPlayerSso", LoggedInPlayer.getLoggedInPlayerSso());
        model.addAttribute("loggedInPlayerId", LoggedInPlayer.getLoggedInPlayerId());
        // note: we have 4, so four minus how many already signed up for the game
        //       we also add one to count the current player ...
        int numOfPlayersInGame = game.getOpponents().size() + 1; // 1 for the current player
    	System.out.println("++++++++++++++++++++++++ numOfPlayersInGame = " + numOfPlayersInGame);
        int quorum = (4 - numOfPlayersInGame) == 0 ? 1 : 0;
        
        GameBoard gameBoard = new GameBoard();
        
        gameBoard.setGameId(game.getGameId());

        model.addAttribute("oppSize", 4 - (game.getOpponents().size() + 1)); 
        model.addAttribute("game", game);
        model.addAttribute("gameBoard", gameBoard);
        model.addAttribute("gameStatus", gameService.getGameStatusForPlayer(playerId));
        model.addAttribute("quorum", quorum);
        model.addAttribute("whoseTurnId", game.getWhoseTurnId());

        List<GameMoveDto> gameMoves = null;
        
        if (oldGame == 1) {
            gameMoves = gameService.getGameOldMoves(game.getGameId());
        } else {
            gameMoves = gameService.getGameMoves(playerId);
        }
    	model.addAttribute("gameMoves", gameMoves);
    	
    	// 1. did the player pass cards
    	if(gameService.getGameStatusForPlayer(playerId).equals("W") != true && gameService.didPlayerTrashCards(game.getGameId(), playerId)) {
    		// yes he did
    		model.addAttribute("playerTrashCards", true);
    		
    		// get the cards he trashed
    		List<String> trashCards = gameService.getPlayerTrashCards(game.getGameId(), playerId);
    		
    		model.addAttribute("firstCard", trashCards.get(0));
    		model.addAttribute("secondCard", trashCards.get(1));
    		model.addAttribute("thirdCard", trashCards.get(2));
    		
    		
    		// 2. did all players pass cards, can we play?
    		boolean didAllPlayersTrashCards = gameService.didAllPlayersTrashCards(game.getGameId());
    		
    		if(didAllPlayersTrashCards) {
    			model.addAttribute("allPlayersTrashCards", true);
    		}
    		else {
    			model.addAttribute("allPlayersTrashCards", false);
    		}
    	}
    	else {
    		model.addAttribute("playerTrashCards", false);
    	}
    	
        return "board";
    }

    @RequestMapping(value = "/board", method = RequestMethod.POST)
    public String play(@Valid GameBoard gameBoard, 
    		           BindingResult result, ModelMap model) {
    	
        if (result.hasErrors()) {
            System.out.println("There are errors");
            return "board";
        }
    	
    	int playerId = LoggedInPlayer.getLoggedInPlayerId();
    	int ret = -1;
    	
    	GameDto game = gameService.joinAGame(playerId);
    	
    	if (playerId != game.getWhoseTurnId()) {
    		//we don't accept submissions if it is not your turn.
    		gameBoard.setCardId("");
            model.addAttribute("gameBoard", gameBoard);
			System.out.println("+++++++++++++++++++++++++ board POST play = NOT YOUR TURN " + playerId);
			gameService.setCheaterMsg(playerId, gameBoard.getGameId(), LoggedInPlayer.getLoggedInPlayerSso() + " tried to play when it is not their turn.");
    		return board(-1,model);
    	}
    	
    	// First mark the card that it has been used
		System.out.println("+++++++++++++++++++++++++ board POST play = " + playerId);
    	System.out.println("+++++++++++++++++++++++++ playerId = " + playerId);
    	System.out.println("+++++++++++++++++++++++++ gameboard.gameId = " + gameBoard.getGameId());
    	System.out.println("+++++++++++++++++++++++++ gameboard.cardId = " + gameBoard.getCardId());
    	gameBoard.setCardId(gameBoard.getCardId().replaceAll("'", ""));
    	System.out.println("+++++++++++++++++++++++++ cardId = " + gameBoard.getCardId());
    	
    	List<String> playerCards = gameService.getPlayerCards(playerId, gameBoard.getGameId());
    	int found = 0;
    	
    	//loop through the list to determine if the player has the card
		for (int i = 0; i < playerCards.size(); i++) {
			if (playerCards.get(i).equals(gameBoard.getCardId())) {
				found=1;
				i=playerCards.size();
			}
		}
		
		if (found==1) {
	    	//player has the card.	    	
	    	ret = gameService.play(playerId, gameBoard.getGameId(), gameBoard.getCardId());
			System.out.println("+++++++++++++++++++++++++ board POST play = VALID CARD " + gameBoard.getCardId());
		} else {
			//notify users and prevent card from being played.
			String cheat = LoggedInPlayer.getLoggedInPlayerSso() + " did not submit a valid card from their hand.";
			gameService.setCheaterMsg(playerId, gameBoard.getGameId(), cheat);
			System.out.println("+++++++++++++++++++++++++ board POST play = INVALID CARD " + gameBoard.getCardId());
    		gameBoard.setCardId("");
            model.addAttribute("gameBoard", gameBoard);
	    	return board(-1, model);
		}
		
        if (ret == 0) {
        	gameService.joinAGame(playerId);
		    gameService.setCheaterMsg(playerId, gameBoard.getGameId(), null);
        }
    	return board(-1, model);
    }
    
    private void authenticateUserAndSetSession(Player player, HttpServletRequest request) {
        
        String username = player.getSsoId();
        String password = player.getPassword();
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);

        // generate session if one doesn't exist
        request.getSession();

        token.setDetails(new WebAuthenticationDetails(request));
        Authentication authenticatedUser = authenticationManager.authenticate(token);

        SecurityContextHolder.getContext().setAuthentication(authenticatedUser);
    }
}
