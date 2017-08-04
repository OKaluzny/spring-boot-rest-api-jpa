DROP TABLE IF EXISTS public.books;
CREATE TABLE public.books (
  book_id INTEGER PRIMARY KEY NOT NULL,
  creation_date TIMESTAMP WITHOUT TIME ZONE,
  modified_date TIMESTAMP WITHOUT TIME ZONE,
  book_name CHARACTER VARYING(50),
  pages BIGINT,
  publishing_year INTEGER,
  author_fk INTEGER,
  publisher_fk INTEGER,
  FOREIGN KEY (author_fk) REFERENCES public.authors (author_id)
  MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  FOREIGN KEY (publisher_fk) REFERENCES public.publishers (publisher_id)
  MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

DROP TABLE IF EXISTS public.authors;
CREATE TABLE public.authors (
  author_id INTEGER PRIMARY KEY NOT NULL,
  creation_date TIMESTAMP WITHOUT TIME ZONE,
  modified_date TIMESTAMP WITHOUT TIME ZONE,
  book_count INTEGER,
  author_name CHARACTER VARYING(255),
  address_fk INTEGER,
  FOREIGN KEY (address_fk) REFERENCES public.addresses (address_id)
  MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);

DROP TABLE IF EXISTS public.publishers;
CREATE TABLE public.publishers (
  publisher_id INTEGER PRIMARY KEY NOT NULL,
  creation_date TIMESTAMP WITHOUT TIME ZONE,
  modified_date TIMESTAMP WITHOUT TIME ZONE,
  book_count INTEGER,
  publisher_name CHARACTER VARYING(255)
);

DROP TABLE IF EXISTS public.addresses;
CREATE TABLE public.addresses (
  address_id INTEGER PRIMARY KEY NOT NULL,
  creation_date TIMESTAMP WITHOUT TIME ZONE,
  modified_date TIMESTAMP WITHOUT TIME ZONE,
  author_city CHARACTER VARYING(255),
  author_country CHARACTER VARYING(255)
);

DROP TABLE IF EXISTS public.author_publisher;
CREATE TABLE public.author_publisher (
  author_fk INTEGER NOT NULL,
  publisher_fk INTEGER NOT NULL,
  PRIMARY KEY (author_fk, publisher_fk),
  FOREIGN KEY (publisher_fk) REFERENCES public.publishers (publisher_id)
  MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION,
  FOREIGN KEY (author_fk) REFERENCES public.authors (author_id)
  MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
);
