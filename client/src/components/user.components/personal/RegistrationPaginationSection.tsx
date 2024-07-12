import React from "react";
import "./../../../assets/css/registration.css"
import {
  Pagination,
  PaginationContent,
  PaginationEllipsis,
  PaginationItem,
  PaginationLink,
  PaginationNext,
  PaginationPrevious,
} from "../../ui/pagination";

type PropType = {
  totalItems: number;
  itemPerPage: number;
  currentPage: number;
  setCurrentPage: (page: number) => void;
};

export default function RegistrationPaginationSection({
  totalItems,
  itemPerPage,
  currentPage,
  setCurrentPage,
}: PropType) {
  const lastPage = Math.ceil(totalItems / itemPerPage);
  if (lastPage <= 1) return null;

  const handlePageClick = (
    event:
      | React.MouseEvent<HTMLAnchorElement, MouseEvent>
      | React.MouseEvent<HTMLSpanElement, MouseEvent>,
    page: number
  ) => {
    event.preventDefault();
    setCurrentPage(page);
  };

  const startPage = Math.max(2, currentPage - 1);
  const endPage = Math.min(lastPage - 1, currentPage + 1);

  return (
    <Pagination>
      <PaginationContent>
        <PaginationItem>
          {currentPage !== 1 ? (
            <PaginationPrevious
              onClick={(event) =>
                currentPage > 1 && handlePageClick(event, currentPage - 1)
              }
            />
          ) : (
            <PaginationPrevious
              style={{color: "#cbd5e1" }} className="pagination"
            />
          )}
        </PaginationItem>
        <PaginationItem key={1}>
          <PaginationLink
            className={currentPage === 1 ? "bg-violet-600 text-white" : ""}
            onClick={(event) => handlePageClick(event, 1)}
          >
            1
          </PaginationLink>
        </PaginationItem>
        {currentPage > 4 && (
          <PaginationItem className="cursor-pointer">
            <PaginationEllipsis
              className="hover:bg-gray-100"
              onClick={(event) =>
                handlePageClick(event, Math.max(currentPage - 3, 1))
              }
            />
          </PaginationItem>
        )}
        {currentPage === 4 && (
          <PaginationItem key={2}>
            <PaginationLink onClick={(event) => handlePageClick(event, 2)}>
              2
            </PaginationLink>
          </PaginationItem>
        )}
        {Array.from(
          { length: endPage - startPage + 1 },
          (_, i) => startPage + i
        ).map((page) => (
          <PaginationItem key={page}>
            <PaginationLink
              className={currentPage === page ? "bg-violet-600 text-white" : ""}
              onClick={(event) => handlePageClick(event, page)}
            >
              {page}
            </PaginationLink>
          </PaginationItem>
        ))}
        {currentPage < lastPage - 3 && (
          <PaginationItem>
            <PaginationEllipsis
              onClick={(event) =>
                handlePageClick(event, Math.min(currentPage + 3, lastPage))
              }
            />
          </PaginationItem>
        )}
        {currentPage === lastPage - 3 && (
          <PaginationItem key={lastPage - 1}>
            <PaginationLink
              onClick={(event) => handlePageClick(event, lastPage - 1)}
            >
              {lastPage - 1}
            </PaginationLink>
          </PaginationItem>
        )}
        <PaginationItem key={lastPage}>
          <PaginationLink
            className={
              currentPage === lastPage ? "bg-violet-600 text-white" : ""
            }
            onClick={(event) => handlePageClick(event, lastPage)}
          >
            {lastPage}
          </PaginationLink>
        </PaginationItem>
          <PaginationItem>
            {currentPage ===lastPage ? (
              <PaginationNext className="pagination"
                style={{color: "#cbd5e1" }}
              />
            ) : (
              <PaginationNext
                onClick={(event) =>
                  currentPage < lastPage &&
                  handlePageClick(event, currentPage + 1)
                }

              />
            )}
          </PaginationItem>
      </PaginationContent>
    </Pagination>
  );
}
