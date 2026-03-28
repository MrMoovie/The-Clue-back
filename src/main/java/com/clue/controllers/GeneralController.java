package com.clue.controllers;

import com.clue.entities.BaseEntity;
import com.clue.entities.GameEntity;
import com.clue.entities.PlayerEntity;
import com.clue.entities.SuspectEntity;
import com.clue.entities.template.*;
import com.clue.responses.*;
import com.clue.service.Gemini;
import com.clue.service.Persist;
import com.clue.utils.GeneralUtils;
import com.github.javafaker.GameOfThrones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.persistence.Basic;
import javax.sql.DataSource;

import java.util.Collections;
import java.util.List;

import static com.clue.utils.Constants.*;
import static com.clue.utils.Errors.*;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;

@RestController
public class GeneralController {
    @Autowired
    private Persist persist;

    private final Gemini gemini = new Gemini();

    @PostConstruct
    public void init() {
    }

//    @RequestMapping("/login")
//    public BasicResponse getUser(String username, String password) {
//        try {
//            if (username == null || password == null) {
//                return new BasicResponse(false, ERROR_MISSING_USERNAME_OR_PASSWORD);
//            }
//
//            PlayerEntity playerEntity = persist.getUserByUsernameAndPassword(username, password);
//
//            if (playerEntity == null) {
//                return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
//            }
//
//            String token = GeneralUtils.hashMd5(username, password);
//            playerEntity.setToken(token);
//            persist.save(playerEntity);
//
//            return new LoginResponse(true, null, 1, token, playerEntity.getId());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    @RequestMapping("/signup")
//    public BasicResponse addUser(String username, String password, String fullName) {
//        try {
//            if (username == null || password == null || fullName == null ) {
//                return new BasicResponse(false, ERROR_MISSING_VALUES);
//            }
//
//            PlayerEntity existingUser = persist.getPlayerByUsername(username);
//
//            if (existingUser != null) {
//                return new BasicResponse(false, ERROR_USERNAME_ALREADY_EXISTS);
//            }
//
//            PlayerEntity newPlayer = new PlayerEntity();
//            newPlayer.setUsername(username);
//            newPlayer.setPassword(password);
//            newPlayer.setFullName(fullName);
//
//            String token = GeneralUtils.hashMd5(username, password);
//            newPlayer.setToken(token);
//
//            persist.save(newPlayer);
//
//            return new LoginResponse(true, null, 1, token, newPlayer.getId());
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

    @RequestMapping("/login")
    public BasicResponse getUser(String username, String password) {
        try {
            if (username == null || password == null) {
                return new BasicResponse(false, ERROR_MISSING_USERNAME_OR_PASSWORD);
            }

            PlayerEntity playerEntity = persist.getUserByUsernameAndPassword(username, password);

            if (playerEntity == null) {
                return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
            }

            String token = GeneralUtils.hashMd5(username, password);
            playerEntity.setToken(token);
            persist.save(playerEntity);

            return new LoginResponse(true, null, 1, token, playerEntity.getId());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @RequestMapping("/signup")
    public BasicResponse addUser(String username, String password, String fullName) {
        try {
            if (username == null || password == null || fullName == null) {
                return new BasicResponse(false, ERROR_MISSING_VALUES);
            }

            PlayerEntity existingUser = persist.getPlayerByUsername(username);

            if (existingUser != null) {
                return new BasicResponse(false, ERROR_USERNAME_ALREADY_EXISTS);
            }

            PlayerEntity newPlayer = new PlayerEntity();
            newPlayer.setUsername(username);
            newPlayer.setPassword(password);
            newPlayer.setFullName(fullName);

            String token = GeneralUtils.hashMd5(username, password);
            newPlayer.setToken(token);

            persist.save(newPlayer);

            return new LoginResponse(true, null, 1, token, newPlayer.getId());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    //-----------------------------------------------------------------------------

    @RequestMapping("/start-game")
    public BasicResponse startGame(String token, String topic) throws Exception {
        PlayerEntity player = persist.getPlayerByToken(token);
        if (player == null) {
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }
        if (topic == null) {
            return new BasicResponse(false, ERROR_MISSING_VALUES);
        }
        GameEntity game = gemini.generateNewGame(topic);
        game.setPlayerEntity(player);

        persist.save(game);

        return new GameResponse(true, null, game);
    }

    //-------------------------------------------------------------------------------

    @RequestMapping("/get-chat-history")
    public BasicResponse getChatHistory(String token, Integer suspectID) {
        PlayerEntity player = persist.getPlayerByToken(token);
        if (player == null) {
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }
        if (suspectID == null) {
            return new BasicResponse(false, ERROR_MISSING_VALUES);
        }
        SuspectEntity suspect = persist.getPlayerSuspect(player.getId(), suspectID);
        if (suspect == null) {
            return new BasicResponse(false, ERROR_SUSPECT_NOT_FOUND);
        }
        return new ChatHistoryResponse(true, null, suspect.getChatHistory());
    }

    @RequestMapping("/chat-with")
    public BasicResponse chatWith(String token, String message, Integer suspectID) throws Exception {
        PlayerEntity player = persist.getPlayerByToken(token);
        if (player == null) {
            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
        }
        if (message == null || suspectID == null) {
            return new BasicResponse(false, ERROR_MISSING_VALUES);
        }

        SuspectEntity suspect = persist.getPlayerSuspect(player.getId(), suspectID);
        suspect.addChatHistory(ROLE_USER, message);
        String response = gemini.chatWith(suspect);
        suspect.addChatHistory(ROLE_MODEL, response);

        return new ChatWithResponse(true, null, response);
    }

    //------------------------------------------------------------------------------------


    // Inject the DataSource that Spring Boot has already configured for you
    @Autowired
    private DataSource dataSource;

    @RequestMapping("/test")
    public String runTestSql() {
        try {
            ClassPathResource resource = new ClassPathResource("test.sql");

            ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator(resource);

            databasePopulator.execute(dataSource);

            return "Successfully executed test.sql! Database is populated with fake data.";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error executing test.sql: " + e.getMessage();
        }
    }


//    @RequestMapping("/get-user-posts")
//    public BasicResponse getUserPosts(String token) {
//        ClientEntity clientEntity = persist.getClientByToken(token);
//        if (clientEntity != null) {
//            List<PostEntity> posts = persist.getPostsByClientId(clientEntity.getId()).stream().filter(post -> !post.isDeleted()).toList();
//            List<BidEntity> myProposals = persist.getProposalsByClientId(clientEntity.getId());
//            return new ClientPostsResponse(true, null, posts, myProposals);
//        } else {
//            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
//        }
//    }
//
//    @RequestMapping("/get-all-posts")
//    public BasicResponse getAllPosts(String token) {
//        ProffesionalEntity proffesionalEntity = persist.getProfessionalByToken(token);
//        if (proffesionalEntity != null) {
//            List<PostEntity> posts = persist.getAllPost().stream().filter(post -> !post.isDeleted()).toList();
//            List<BidEntity> bids = persist.getBidsByProfessionalId(proffesionalEntity.getId());
//            return new ProffesionalPostsResponse(true, null, posts, bids);
//        } else {
//            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
//        }
//    }
//
//    @RequestMapping("/get-all-categories")
//    public BasicResponse getAllCategories(String token) {
//        PlayerEntity playerEntity = persist.getUserByToken(token);
//        if (playerEntity != null) {
//            List<CategoryEntity> categories = persist.getAllCategories().stream().filter(category -> !category.isDeleted()).toList();
//            return new CategoriesResponse(true, null, categories);
//        } else {
//            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
//        }
//    }
//
//    @RequestMapping("/get-default-params")
//    public BasicResponse getDefaultParams(String token) {
//        PlayerEntity playerEntity = persist.getUserByToken(token);
//        if (playerEntity != null) {
//            return new DefaultParamResponse(true, null, playerEntity);
//        } else {
//            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
//        }
//    }
//
//    @RequestMapping("/add-post")
//    public BasicResponse addPost(String token, String text, String fileLink, String area, int categoryId) {
//        ClientEntity clientEntity = persist.getClientByToken(token);
//        CategoryEntity categoryEntity = persist.getCategoryByCategoryId(categoryId);
//        if (clientEntity != null) {
//            PostEntity postEntity = new PostEntity();
//            postEntity.setClientEntity(clientEntity);
//            postEntity.setText(text);
//            postEntity.setArea(area);
//            postEntity.setFileLink(fileLink);
//            if (categoryEntity != null) {
//                postEntity.setCategoryEntity(categoryEntity);
//            }
//            persist.save(postEntity);
//            return new BasicResponse(true, null);
//        } else {
//            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
//        }
//    }
//
//    @RequestMapping("/delete-post")
//    public BasicResponse deletePost(String token, int postId) {
//        ClientEntity clientEntity = persist.getClientByToken(token);
//        if (clientEntity != null) {
//            PostEntity postEntity = persist.getPostByPostId(postId);
//            if (clientEntity.getId() == postEntity.getClientEntity().getId()) {
//                postEntity.setDeleted(true);
//                persist.save(postEntity);
//                return new BasicResponse(true, null);
//            } else {
//                return new BasicResponse(false, ERROR_NOT_AUTHORIZED);
//            }
//        } else {
//            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
//        }
//    }
//
//
//    @RequestMapping("/get-all-users")
//    public List<ClientEntity> getAllUsers() {
//        return persist.loadList(ClientEntity.class);
//    }
//
//
//    @RequestMapping("/make-bid")
//    public BasicResponse makeBid(String token, int postId, float proposedPrice, String description) {
//        ProffesionalEntity proffesionalEntity = persist.getProfessionalByToken(token);
//        if (proffesionalEntity != null) {
//            PostEntity postEntity = persist.getPostByPostId(postId);
//            if (postEntity != null) {
//                BidEntity bidEntity = new BidEntity();
//                bidEntity.setProffesionalEntity(proffesionalEntity);
//                bidEntity.setPostEntity(postEntity);
//                bidEntity.setProposedPrice(proposedPrice);
//                bidEntity.setStatus(0);
//                bidEntity.setDescription(description);
//                persist.save(bidEntity);
//                return new BasicResponse(true, null);
//            } else {
//                return new BasicResponse(false, ERROR_POST_NOT_FOUND);
//            }
//        } else {
//            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
//        }
//    }
//
//
//    @RequestMapping("/my-bids")
//    public BasicResponse myBids(String token) {
//        ProffesionalEntity proffesionalEntity = persist.getProfessionalByToken(token);
//        if (proffesionalEntity != null) {
//            List<BidEntity> myBids = persist.getBidsByProfessionalId(proffesionalEntity.getId());
//            return new BidsResponseModel(true, null, myBids);
//        } else {
//            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
//        }
//    }
//
//    @RequestMapping("/my-proposals")
//    public BasicResponse myProposals(String token) {
//        ClientEntity clientEntity = persist.getClientByToken(token);
//        if (clientEntity != null) {
//            List<BidEntity> myProposals = persist.getProposalsByClientId(clientEntity.getId()).stream().filter(proposal -> !proposal.getPostEntity().isDeleted()).toList();
//            return new BidsResponseModel(true, null, myProposals);
//        } else {
//            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
//        }
//    }
//
//    @RequestMapping("/get-post-user")
//    public BasicResponse getUserPost(String token, int id) {
//        ClientEntity clientEntity = persist.getClientByToken(token);
//        if (clientEntity != null) {
//            PostEntity postEntity = persist.loadObject(PostEntity.class, id);
//            List<BidEntity> myProposals = persist.getProposalsByClientId(clientEntity.getId());
//            return new ClientPostResponse(true, null, postEntity, myProposals);
//        } else {
//            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
//        }
//    }
//
//    @RequestMapping("/get-post-professional")
//    public BasicResponse getProfessionalPost(String token, int id) {
//        ProffesionalEntity proffesionalEntity = persist.getProfessionalByToken(token);
//        if (proffesionalEntity != null) {
//            PostEntity postEntity = persist.loadObject(PostEntity.class, id);
//            List<BidEntity> bids = persist.getBidsByProfessionalId(proffesionalEntity.getId());
//            return new ProffesionalPostResponse(true, null, postEntity, bids);
//        } else {
//            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
//        }
//    }
//
//    @RequestMapping("/get-bid")
//    public BasicResponse getBid(String token, int id) {
//        BaseEntity baseEntity = persist.getUserByToken(token);
//        if (baseEntity != null) {
//            BidEntity bidEntity = persist.loadObject(BidEntity.class, id);
//            List<MessageEntity> conversation = persist.getConversation(bidEntity.getId());
//            Collections.reverse(conversation);
//            return new BidResponse(true, null, bidEntity, conversation);
//        } else {
//            return new BasicResponse(false, ERROR_WRONG_CREDENTIALS);
//        }
//    }


}
