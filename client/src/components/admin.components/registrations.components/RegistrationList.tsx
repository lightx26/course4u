import { RegistrationType } from "../../../App";
import { SkeletonLoader } from "./RegistrationCard";
import React from "react";

const RegistrationCardComponent = React.lazy(
  () => import("./RegistrationCard")
);

type PropsType = {
  ListRegistration: RegistrationType[];
  isLoading: boolean;
  numRegistration?: number;
};

function RegistrationList({
  ListRegistration,
  isLoading,
  numRegistration = 8,
}: PropsType) {
  if (isLoading) {
    return (
      <div className="grid gap-2 text-black xl:grid-cols-4 md:grid-cols-2 auto-rows-auto">
        {Array.from({ length: numRegistration }).map((_, index) => (
          <SkeletonLoader key={`skeleton-${index}`} />
        ))}
      </div>
    );
  } else {
    if(!ListRegistration || ListRegistration.length === 0){
      return <div className="text-center">No registration found</div>
    }

    return (
      <div className="grid gap-2 text-black xl:grid-cols-4 md:grid-cols-2 auto-rows-auto">
        {ListRegistration.map((registration) => (
          <RegistrationCardComponent
            registration={registration}
            key={registration.id}
          />
        ))}
      </div>
    );
  }
}

export default RegistrationList;
