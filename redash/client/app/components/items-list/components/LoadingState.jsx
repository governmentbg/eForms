import React from "react";
import BigMessage from "@/components/BigMessage";
import { t } from "@/locales/config.jsx";

// Default "loading" message for list pages
export default function LoadingState(props) {
  return (
    <div className="text-center">
      <BigMessage icon="fa-spinner fa-2x fa-pulse" message={t("Loading...")} {...props} />
    </div>
  );
}
