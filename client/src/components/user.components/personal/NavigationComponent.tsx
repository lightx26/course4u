import React from 'react';
import { NavLink } from 'react-router-dom';
import styled from 'styled-components';

const Container = styled.div`
  font-family: Arial, sans-serif;

  @media (min-width: 768px) {
    .box {
      padding-left: 1.2rem;
    }
  }

  @media (min-width: 1888px) {
    .box {
      padding-left: 12rem;
    }
  }

`;
const Navigation: React.FC = () => {
  return (
    <Container>
    <div className="box">
      <nav className="w-full max-w-3xl mt-5 items-start ">
        <ul className="flex border-b-2 border-gray-200">
          <li className="flex-1 flex justify-center">
            <NavLink
              to="/personal/account"
              end
              className={({ isActive }) =>
                isActive ? "text-[#9B47B2] border-[#9B47B2] border-b-4 pb-2 w-full text-center" : "text-[#000000] border-transparent border-b-4 pb-2 w-full text-center"
              }
            >
              My profile
            </NavLink>
          </li>
          <li className="flex-1 flex justify-center">
            <NavLink
              to="/personal/registration"
              className={({ isActive }) =>
                isActive ? "text-[#9B47B2] border-[#9B47B2] border-b-4 pb-2 w-full text-center" : "text-[#000000] border-transparent border-b-4 pb-2 w-full text-center"
              }
            >
              My registration
            </NavLink>
          </li>
          <li className="flex-1 flex justify-center">
            <NavLink
              to="/personal/score"
              className={({ isActive }) =>
                isActive ? "text-[#9B47B2] border-[#9B47B2] border-b-4 pb-2 w-full text-center" : "text-[#000000] border-transparent border-b-4 pb-2 w-full text-center"
              }
            >
              My score
            </NavLink>
          </li>
        </ul>
      </nav>
    </div>
    </Container>
  );
}

export default Navigation;
