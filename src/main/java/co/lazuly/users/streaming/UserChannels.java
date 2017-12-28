package co.lazuly.users.streaming;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

/**
 * Created by boot on 27/12/2017.
 */
public interface UserChannels {
    String NEW_OWNER_INPUT = "new_owner_input";
    String NEW_USER_OUTPUT = "new_user_output";
    String DELETE_USER_OUTPUT = "delete_user_output";
    String CHANGE_ROLES_OUTPUT = "change_roles_output";

    @Input(NEW_OWNER_INPUT)
    SubscribableChannel newOwnerInput();

    @Output(NEW_USER_OUTPUT)
    MessageChannel newUserOutput();

    @Output(DELETE_USER_OUTPUT)
    MessageChannel deleteUserOutput();

    @Output(CHANGE_ROLES_OUTPUT)
    MessageChannel changeRolesOutput();
}
