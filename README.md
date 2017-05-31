# Opis

Program służy do przedstawienia statystyki odczuwanych wyrzutów sumienia przy
robieniu zakupów. Tak przy wprowadzeniu poszczególnych zakupów do danego
programu, oprócz podania szczegółów (nazwa, data, cena, waluta) przypisujemy
również poziom wyrzutów sumienia, odczywanych przy wydaniu pieniędzy na te
zakupy. W wyniku analizy tych danych możemy zobaczyć jaki poziom wyrzutów nam
towarzyszył najczęściej, a z jakim poziomem robiliśmy najmniej zakupów w skali
procentowej, a także sumaryczne wydatki przy każdemu z poziomów sumienia za
określony w ustawieniach aplikacji czas.


# Wykaz funkcjonalności

W głównej aktywności znajdują się dwa przyciski, pierwszy z których ma nazwę
"Otwórz wykres" otwiera wykres odczuwanych wyrzutów sumienia oraz informację o
poszczególnych poziomach danych wyrzutów na podstawie przeanalizowanych danych
za określony czas. Drugi, z nazwą "Nowy zakup" umożliwia wprowadzenie do bazy
danych aplikacji informacji o nowym zakupie. Tak przy kliknięciu tego przycisku
otworzy się aktywność z polami umożliwiającymi wprowadzenie danych o nowym
zakupie: nazwę, datę (domyślnie w tym polu ustawiona jest data bieżąca), i cene
(w przypadku liczby ułamkowej, część całkowitą należy odzielić kropką). W tej
aktywności również znajduje się skala poziomów wyrzutów sumienia (jest ich 5)
jeden z których należy wybrać, oraz 2 przyciski, jeden z nich o nazwie wybranej
waluty (domyślnie PLN), przy naciśnięciu którego otworzy się lista dostępnych
walut, pobranych ze strony internetowej Narodowego Banku Polskiego. Przycisk ten
umożliwia zmianę domyślnej waluty ceny dodawanego zakupu. Drugi przycisk o nazwie
"Zapisz" dodaje wprowadzone informacje do bazy danych i uruchamia główną
aktywność. W głównej aktywności znajduje się również lista dodanych informacji o
zrobionych zakupach za określony czas. Przy kliknięciu na dowolnym z nich
wyświetli się informacja z datą i czasem zrobienia danego zakupu. Natomiast przy
przytrzymaniu nacisku na dowolnym z wyświetlonych zakupów, program zapyta
użytkownika czy dany zakup ma być usunięty z bazy danych, i przy kliknięciu
przycisku pod nazwą "TAK" program usunie wszystkie informacje, związane z danych
zakupem. W głownej aktywności znajduje się również menu ekranowe o nazwie
"Ustawienia", przy kliknięciu na niego otworzy się ekran ustawień. Pierwsza opcja
tego ekranu o nazwie "Okres zakupów" umożliwia zmianę okresu czasu na podstawie
którego odczytywane będą dane o poszczególnych zakupach z bazy danych dla podalszej
analizy. Druga opcja ekranu ustawień ma nazwę "Hasło dostępu" i umożliwia
ustawienia, zmianę oraz usunięcia hasła dostępu, które będzie sprawdzanie
każdorazowo przy próbie uruchomienia danej aplikacji. Trzecia opcja ekranu ustawień
umożliwia usunięcie wszystkich danych o zakupach z bazy danych aplikacji. Usunięcie
należy potwierdzić wciskając przycisk o nazwie "TAK" po wybraniu danej opcji.