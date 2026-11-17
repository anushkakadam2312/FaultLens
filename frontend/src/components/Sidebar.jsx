
import {
  LayoutDashboard,
  Server,
  Activity,
  FileText,
  Bell,
  Settings,
  ShieldCheck
} from "lucide-react";

import "./Sidebar.css";

function Sidebar({ activePage, setActivePage }) {
  const menuItems = [
    { name: "Overview", icon: LayoutDashboard },
    { name: "Services", icon: Server },
    { name: "Fault Events", icon: Activity },
    { name: "Logs", icon: FileText },
    { name: "Alerts", icon: Bell },
  ];

  return (
    <aside className="sidebar">

      <div className="sidebar-logo">
        <ShieldCheck size={30} />
        <h2>FaultLens</h2>
      </div>

      <p className="sidebar-label">MONITORING</p>

      <nav>
        {menuItems.map((item) => {
          const Icon = item.icon;

          return (
            <button
              key={item.name}
              className={`sidebar-link ${
                activePage === item.name ? "active" : ""
              }`}
              onClick={() => setActivePage(item.name)}
            >
              <Icon size={19} />
              <span>{item.name}</span>
            </button>
          );
        })}
      </nav>

      <div className="sidebar-bottom">
          <button
              className={`sidebar-link ${
              activePage === "Settings" ? "active" : ""
               }`}
        onClick={() => setActivePage("Settings")}
      >
          <Settings size={19} />
          <span>Settings</span>
        </button>

        <div className="sidebar-version">
          FaultLens v1.0
        </div>
      </div>

    </aside>
  );
}

export default Sidebar;
