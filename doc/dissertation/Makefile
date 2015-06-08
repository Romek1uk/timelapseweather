DISS = diss.tex refs.bib makefile.txt

help:
	@echo
	@echo "USAGE:"
	@echo
	@echo "make          display help information"
	@echo "make diss.pdf make a .pdf version of the dissertation"
	@echo "make view     view the dissertation with zathura"
	@echo "make all      construct everything, clean directory and display estimated word count"
	@echo "make count    display an estimated word count"
	@echo "make clean    remove all remakeable files"
	@echo "make fast     cleans and makes with a single iteration"
	@echo "make editor   open zathura and vim to edit dissertation"
	@echo

diss.pdf:	${DISS}
	pdflatex diss
	bibtex diss
	pdflatex diss
	bibtex diss
	pdflatex diss
	bibtex diss
	pdflatex diss.tex

fast:
	pdflatex diss.tex

makefile.txt:	Makefile
	expand Makefile >makefile.txt
	
count:
	detex diss.tex | tr -cd '0-9A-Za-z \n' | wc -w

all: diss.pdf
	make clean
	@echo
	make count
	@echo

clean:
	rm -f diss.ps *.dvi *.aux *.log *.err
	rm -f core *~ *.lof *.toc *.blg *.bbl
	rm -f makefile.txt
	rm -f ./snippets/*.aux

view: 
	zathura "$@" 2>/dev/null diss.pdf&
	
editor: view
	vim diss.tex
