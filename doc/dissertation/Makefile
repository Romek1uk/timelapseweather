DISS = diss.tex refs.bib makefile.txt

help:
	@echo
	@echo "USAGE:"
	@echo
	@echo "make          display help information"
	@echo "make diss.ps  make a postscript version of the dissertation"
	@echo "make diss.pdf make a .pdf version of the dissertation"
	@echo "make view     view the dissertation with zathura"
	@echo "make all      construct everything"
	@echo "make count    display an estimated word count"
	@echo "make clean    remove all remakeable files"
	@echo "make editor   open zathura and vim to edit dissertation"
	@echo

diss.ps:	$(DISS)
	latex diss
	bibtex diss
	latex diss
	bibtex diss
	latex diss
	bibtex diss
	dvips -Ppdf -G0 -t a4 -pp 0-200 -o diss.ps diss.dvi

diss.pdf:	diss.ps
	ps2pdf diss.ps

makefile.txt:	Makefile
	expand Makefile >makefile.txt
	
count:
	detex diss.tex | tr -cd '0-9A-Za-z \n' | wc -w

all:	diss.ps

clean:
	rm -f diss.ps *.dvi *.aux *.log *.err
	rm -f core *~ *.lof *.toc *.blg *.bbl
	rm -f makefile.txt

view: diss.ps
	zathura diss.pdf&
	
editor:
	zathura diss.pdf&
	vim diss.tex
