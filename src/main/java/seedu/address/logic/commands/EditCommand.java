package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE_OF_RELEASE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DESCRIPTION;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_IS_WATCHED;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RUNNING_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ACTOR;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_SHOWS;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.show.Description;
import seedu.address.model.show.RunningTime;
import seedu.address.model.show.Name;
import seedu.address.model.show.Show;
import seedu.address.model.show.Date;
import seedu.address.model.show.IsWatched;
import seedu.address.model.actor.Actor;

/**
 * Edits the details of an existing show in the watchlist.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the show identified "
            + "by the index number used in the displayed show list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_DATE_OF_RELEASE + "DATE OF RELEASE] "
            + "[" + PREFIX_IS_WATCHED + "WATCHED?] "
            + "[" + PREFIX_DESCRIPTION + "DESCRIPTION] "
            + "[" + PREFIX_RUNNING_TIME + "RUNNING TIME] "
            + "[" + PREFIX_ACTOR + "ACTOR]...\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_NAME + "Joker "
            + PREFIX_DATE_OF_RELEASE + "3 October 2019";

    public static final String MESSAGE_EDIT_SHOW_SUCCESS = "Edited Show: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_SHOW = "This show already exists in the watchlist.";

    private final Index index;
    private final EditShowDescriptor editShowDescriptor;

    /**
     * @param index of the show in the filtered show list to edit
     * @param editShowDescriptor details to edit the show with
     */
    public EditCommand(Index index, EditShowDescriptor editShowDescriptor) {
        requireNonNull(index);
        requireNonNull(editShowDescriptor);

        this.index = index;
        this.editShowDescriptor = new EditShowDescriptor(editShowDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Show> lastShownList = model.getFilteredShowList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_SHOW_DISPLAYED_INDEX);
        }

        Show showToEdit = lastShownList.get(index.getZeroBased());
        Show editedShow = createEditedShow(showToEdit, editShowDescriptor);

        if (!showToEdit.isSameShow(editedShow) && model.hasShow(editedShow)) {
            throw new CommandException(MESSAGE_DUPLICATE_SHOW);
        }

        model.setShow(showToEdit, editedShow);
        model.updateFilteredShowList(PREDICATE_SHOW_ALL_SHOWS);
        return new CommandResult(String.format(MESSAGE_EDIT_SHOW_SUCCESS, editedShow));
    }

    /**
     * Creates and returns a {@code Show} with the details of {@code showToEdit}
     * edited with {@code editShowDescriptor}.
     */
    private static Show createEditedShow(Show showToEdit, EditShowDescriptor editShowDescriptor) {
        assert showToEdit != null;

        Name updatedName = editShowDescriptor.getName().orElse(showToEdit.getName());
        Date updatedDateOfRelease = editShowDescriptor.getDateOfRelease().orElse(showToEdit.getDateOfRelease());
        IsWatched updatedIsWatched = editShowDescriptor.getIsWatched().orElse(showToEdit.isWatched());
        Description updatedDescription = editShowDescriptor.getDescription().orElse(showToEdit.getDescription());
        RunningTime updatedRunningTime = editShowDescriptor.getRunningTime().orElse(showToEdit.getRunningTime());
        Set<Actor> updatedActors = editShowDescriptor.getActors().orElse(showToEdit.getActors());

        return new Show(updatedName, updatedDescription, updatedIsWatched,
                updatedDateOfRelease, updatedRunningTime, updatedActors);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
                && editShowDescriptor.equals(e.editShowDescriptor);
    }

    /**
     * Stores the details to edit the show with. Each non-empty field value will replace the
     * corresponding field value of the show.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Date dateOfRelease;
        private IsWatched isWatched;
        private Description description;
        private RunningTime runningTime;
        private Set<Actor> actors = new HashSet<>();

        public EditShowDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code actors} is used internally.
         */
        public EditShowDescriptor(EditShowDescriptor toCopy) {
            setName(toCopy.name);
            setDateOfRelease(toCopy.dateOfRelease);
            setIsWatched(toCopy.isWatched);
            setDescription(toCopy.description);
            setRunningTime(toCopy.runningTime);
            setActors(toCopy.actors);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, dateOfRelease, isWatched, description, runningTime, actors);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            // state check
            EditPersonDescriptor e = (EditPersonDescriptor) other;

            return getName().equals(e.getName())
                    && getPhone().equals(e.getPhone())
                    && getEmail().equals(e.getEmail())
                    && getAddress().equals(e.getAddress())
                    && getTags().equals(e.getTags());
        }
    }
}
