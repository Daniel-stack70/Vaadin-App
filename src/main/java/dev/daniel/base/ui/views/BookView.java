package dev.daniel.base.ui.views;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import dev.daniel.base.ui.component.ViewToolbar;
import dev.daniel.entity.Author;
import dev.daniel.entity.Book;
import dev.daniel.entity.Category;
import dev.daniel.services.AuthorService;
import dev.daniel.services.BookService;
import dev.daniel.services.CategoryService;

import java.util.stream.Stream;

@Route("book-list")
@PageTitle("Books")
@Menu(order = -100, icon = "vaadin:home", title = "Book List")
public class BookView extends Main {

    private final BookService bookService;
    private final AuthorService authorService;
    private final CategoryService categoryService;

    private final Grid<Book> bookGrid;

    private final TextField title;
    private final TextField ratingField;
    private final ComboBox<Author> authorCombo;
    private final ComboBox<Category> categoryCombo;
    private final Button createBtn;

    private final FormLayout editorForm;
    private final TextField titleField;
    private final TextField ratingEditField;
    private final ComboBox<Author> authorField;
    private final ComboBox<Category> categoryField;
    private final Button updateBtn;
    private final Button deleteBtn;

    private final FormLayout sideEditorForm;
    private final TextField sideTitle;
    private final TextField sideRating;
    private final ComboBox<Author> sideAuthorCombo;
    private final ComboBox<Category> sideCategoryCombo;
    private final Button sideUpdateBtn;
    private final Button sideDeleteBtn;

    private Book selectedBook;

    public BookView(BookService bookService, AuthorService authorService, CategoryService categoryService) {
        this.bookService = bookService;
        this.authorService = authorService;
        this.categoryService = categoryService;

        title = new TextField("Title");
        ratingField = new TextField("Rating");

        authorCombo = createAuthorCombo();
        authorCombo.addCustomValueSetListener(event -> {
            String newAuthorName = event.getDetail();
            if (newAuthorName != null && !newAuthorName.isBlank()) {
                Author newAuthor = authorService.saveAuthor(newAuthorName);
                authorCombo.setValue(newAuthor);
            }
        });

        categoryCombo = createCategoryCombo();
        categoryCombo.addCustomValueSetListener(event -> {
            String newCategoryName = event.getDetail();
            if (newCategoryName != null && !newCategoryName.isBlank()) {
                Category newCategory = categoryService.saveCategory(newCategoryName);
                categoryCombo.setValue(newCategory);
            }
        });

        createBtn = new Button("Create", event -> addBook());
        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout toolbarLayout =
                new HorizontalLayout(title, authorCombo, categoryCombo, ratingField, createBtn);

        toolbarLayout.setVerticalComponentAlignment(
                FlexComponent.Alignment.END, createBtn
        );
        editorForm = new FormLayout();
        editorForm.setVisible(false);

        titleField = new TextField("Title");
        ratingEditField = new TextField("Rating");

        authorField = createAuthorCombo();
        authorField.addCustomValueSetListener(event -> {
            String newAuthorName = event.getDetail();
            if (newAuthorName != null && !newAuthorName.isBlank()) {
                Author newAuthor = authorService.saveAuthor(newAuthorName);
                authorField.setValue(newAuthor);
            }
        });

        categoryField = createCategoryCombo();
        categoryField.addCustomValueSetListener(event -> {
            String newCategoryName = event.getDetail();
            if (newCategoryName != null && !newCategoryName.isBlank()) {
                Category newCategory = categoryService.saveCategory(newCategoryName);
                categoryField.setValue(newCategory);
            }
        });

        updateBtn = new Button("Update", e -> updateBook());
        deleteBtn = new Button("Delete", e -> deleteBook());
        deleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

        editorForm.add(titleField, ratingEditField, authorField, categoryField, updateBtn, deleteBtn);

        sideEditorForm = new FormLayout();
        sideEditorForm.setVisible(false);

        sideTitle = new TextField("Title");
        sideRating = new TextField("Rating");

        sideAuthorCombo = createAuthorCombo();
        sideAuthorCombo.addCustomValueSetListener(event -> {
            String newAuthorName = event.getDetail();
            if (newAuthorName != null && !newAuthorName.isBlank()) {
                Author newAuthor = authorService.saveAuthor(newAuthorName);
                sideAuthorCombo.setValue(newAuthor);
            }
        });

        sideCategoryCombo = createCategoryCombo();
        sideCategoryCombo.addCustomValueSetListener(event -> {
            String newCategoryName = event.getDetail();
            if (newCategoryName != null && !newCategoryName.isBlank()) {
                Category newCategory = categoryService.saveCategory(newCategoryName);
                sideCategoryCombo.setValue(newCategory);
            }
        });

        sideUpdateBtn = new Button("Update", e -> updateBook());
        sideDeleteBtn = new Button("Delete", e -> deleteBook());
        sideDeleteBtn.addThemeVariants(ButtonVariant.LUMO_ERROR);

        sideEditorForm.add(sideTitle, sideRating, sideAuthorCombo, sideCategoryCombo, sideUpdateBtn, sideDeleteBtn);

        bookGrid = new Grid<>();
        //bookGrid.setItems(query -> bookService.getAllBooks(toSpringPageRequest(query)).stream());
        bookGrid.setItems(bookService.getAllBooks());
        bookGrid.addColumn(Book::getTitle).setHeader("Title");
        bookGrid.addColumn(book -> book.getAuthor().getName()).setHeader("Author");
        bookGrid.addColumn(book -> book.getCategory().getName()).setHeader("Category");
        bookGrid.addColumn(book -> String.format("%.2f", book.getBookRating()) ).setHeader("Rating");
        bookGrid.setSizeFull();
        bookGrid.asSingleSelect().addValueChangeListener(event -> {
            selectedBook = event.getValue();
            if (selectedBook != null) {
                sideTitle.setValue(selectedBook.getTitle());
                sideRating.setValue(String.format("%.2f", selectedBook.getBookRating()));
                sideAuthorCombo.setValue(selectedBook.getAuthor());
                sideCategoryCombo.setValue(selectedBook.getCategory());
                sideEditorForm.setVisible(true);
            } else {
                sideEditorForm.setVisible(false);
            }
        });

        setSizeFull();
        addClassNames(
                LumoUtility.BoxSizing.BORDER,
                LumoUtility.Display.FLEX,
                LumoUtility.FlexDirection.COLUMN,
                LumoUtility.Padding.MEDIUM,
                LumoUtility.Gap.SMALL
        );

        add(new ViewToolbar("Book List", toolbarLayout));
        add(bookGrid, editorForm, sideEditorForm);
    }

    private ComboBox<Author> createAuthorCombo() {
        ComboBox<Author> combo = new ComboBox<>("Author");
        combo.setItemLabelGenerator(Author::getName);
        combo.setAllowCustomValue(true);
        combo.setClearButtonVisible(true);
        combo.setDataProvider(
                (filterText, offset, limit) -> {
                    if (filterText == null || filterText.length() < 3) return Stream.empty();
                    return authorService.findAuthors(filterText, offset, limit).stream();
                },
                filterText -> {
                    if (filterText == null || filterText.length() < 3) return 0;
                    return authorService.countAuthors(filterText);
                }
        );
        return combo;
    }

    private ComboBox<Category> createCategoryCombo() {
        ComboBox<Category> combo = new ComboBox<>("Category");
        combo.setItemLabelGenerator(Category::getName);
        combo.setAllowCustomValue(true);
        combo.setClearButtonVisible(true);
        combo.setDataProvider(
                (filterText, offset, limit) -> {
                    if (filterText == null || filterText.length() < 3) return Stream.empty();
                    return categoryService.findCategories(filterText, offset, limit).stream();
                },
                filterText -> {
                    if (filterText == null || filterText.length() < 3) return 0;
                    return categoryService.countCategories(filterText);
                }
        );
        return combo;
    }

    private void addBook() {
        Author selectedAuthor = authorCombo.getValue();
        Category selectedCategory = categoryCombo.getValue();

        if (selectedAuthor == null || selectedCategory == null) {
            Notification.show("Please select both author and category", 3000, Notification.Position.BOTTOM_END)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        double rating;
        try {
            rating = Double.parseDouble(ratingField.getValue());
        } catch (NumberFormatException e) {
            Notification.show("Please enter a valid rating", 3000, Notification.Position.BOTTOM_END)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        bookService.addBook(title.getValue(), rating, selectedAuthor, selectedCategory);
        bookGrid.getDataProvider().refreshAll();
        title.clear();
        ratingField.clear();
        authorCombo.clear();
        categoryCombo.clear();

        Notification.show("Book added", 3000, Notification.Position.BOTTOM_END)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private void updateBook() {
        if (selectedBook != null) {
            if (editorForm.isVisible()) {
                selectedBook.setTitle(titleField.getValue());
                selectedBook.setBookRating(Double.parseDouble(ratingEditField.getValue()));
                selectedBook.setAuthor(authorField.getValue());
                selectedBook.setCategory(categoryField.getValue());
            }

            if (sideEditorForm.isVisible()) {
                selectedBook.setTitle(sideTitle.getValue());
                selectedBook.setBookRating(Double.parseDouble(sideRating.getValue()));
                selectedBook.setAuthor(sideAuthorCombo.getValue());
                selectedBook.setCategory(sideCategoryCombo.getValue());
            }

            bookService.saveBook(selectedBook);
            bookGrid.getDataProvider().refreshAll();
            Notification.show("Book updated", 3000, Notification.Position.BOTTOM_END)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        }
    }

    private void deleteBook() {
        if (selectedBook != null) {
            bookService.deleteBook(selectedBook);
            selectedBook = null;
            editorForm.setVisible(false);
            sideEditorForm.setVisible(false);
            bookGrid.getDataProvider().refreshAll();
            Notification.show("Book deleted", 3000, Notification.Position.BOTTOM_END)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }
}
