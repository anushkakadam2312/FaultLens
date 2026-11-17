
import { useState, useEffect } from "react";
import {
  Activity,
  Server,
  FileText,
  AlertTriangle,
  CheckCircle,
  Clock,
  Bell
} from "lucide-react";

import Sidebar from "./components/Sidebar";
import { apiGet } from "./services/api";
import "./App.css";

function App() {
  const [activePage, setActivePage] = useState("Overview");

  const [summary, setSummary] = useState(null);
  const [faultEvents, setFaultEvents] = useState([]);
  const [services, setServices] = useState([]);
  const [logs, setLogs] = useState([]);

  const [loading, setLoading] = useState(true);
  const [logsLoading, setLogsLoading] = useState(false);
  const [logsError, setLogsError] = useState("");
  const [error, setError] = useState("");

  // Fetch dashboard data and refresh every 15 seconds
  useEffect(() => {
    const fetchDashboardData = async () => {
      try {
        const [summaryData, eventsData, servicesData] =
          await Promise.all([
            apiGet("/api/dashboard/summary"),
            apiGet("/api/fault-events"),
            apiGet("/api/services")
          ]);

        setSummary(summaryData);

        setFaultEvents(
          Array.isArray(eventsData)
            ? eventsData
            : eventsData ? [eventsData] : []
        );

        setServices(
          Array.isArray(servicesData)
            ? servicesData
            : servicesData ? [servicesData] : []
        );

        setError("");
      } catch (err) {
        setError("Unable to refresh dashboard data.");
        console.error("Dashboard API Error:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchDashboardData();

    const refreshInterval = setInterval(
      fetchDashboardData,
      15000
    );

    return () => clearInterval(refreshInterval);
  }, []);

  // Fetch logs when the Logs page is opened
  useEffect(() => {
    if (activePage !== "Logs") return;

    const fetchLogs = async () => {
      setLogsLoading(true);
      setLogsError("");

      try {
        const data = await apiGet("/api/logs");

        setLogs(
          Array.isArray(data)
            ? data
            : data ? [data] : []
        );
      } catch (err) {
        setLogsError(
          "Unable to load logs. Check whether GET /api/logs is available in your backend."
        );
        console.error("Logs API Error:", err);
      } finally {
        setLogsLoading(false);
      }
    };

    fetchLogs();
  }, [activePage]);

  const stats = [
    {
      title: "Monitored Services",
      value: loading ? "..." : summary?.totalServices ?? 0,
      subtitle: "Services registered",
      icon: Server,
      color: "blue"
    },
    {
      title: "Total Logs",
      value: loading ? "..." : summary?.totalLogs ?? 0,
      subtitle: "Logs ingested",
      icon: FileText,
      color: "purple"
    },
    {
      title: "Open Faults",
      value: loading ? "..." : summary?.totalOpenAlerts ?? 0,
      subtitle: "Requires attention",
      icon: AlertTriangle,
      color: "red"
    },
    {
      title: "Resolved Faults",
      value: loading ? "..." : summary?.totalResolvedAlerts ?? 0,
      subtitle: "Successfully resolved",
      icon: CheckCircle,
      color: "green"
    }
  ];

  const openFaults = faultEvents.filter(
    (event) => event.status === "OPEN"
  );

  const formatDate = (date) => {
    if (!date) return "N/A";
    return new Date(date).toLocaleString();
  };

  const renderOverview = () => (
    <>
      <section className="welcome-section">
        <h2>Welcome to FaultLens</h2>
        <p>
          Your centralized application monitoring
          and fault detection platform.
        </p>
      </section>

      <section className="stats-grid">
        {stats.map((item) => {
          const Icon = item.icon;

          return (
            <div className="stat-card" key={item.title}>
              <div className={`stat-icon ${item.color}`}>
                <Icon size={23} />
              </div>

              <h3>{item.title}</h3>
              <h2>{item.value}</h2>
              <p>{item.subtitle}</p>
            </div>
          );
        })}
      </section>

      {error && <p>{error}</p>}

      <p className="fault-time">
        <Clock size={13} />
        Dashboard automatically refreshes every 15 seconds.
      </p>

      <section className="dashboard-grid">
        <div className="dashboard-panel">
          <div className="panel-header">
            <h3>Recent Fault Events</h3>
            <Activity size={20} />
          </div>

          {loading ? (
            <p>Loading fault events...</p>
          ) : faultEvents.length === 0 ? (
            <p>No fault events found.</p>
          ) : (
            faultEvents.map((event) => {
              const isOpen = event.status === "OPEN";
              const EventIcon = isOpen
                ? AlertTriangle
                : CheckCircle;

              return (
                <div className="fault-item" key={event.id}>
                  <div
                    className={`fault-icon ${
                      isOpen ? "" : "resolved"
                    }`}
                  >
                    <EventIcon size={20} />
                  </div>

                  <div>
                    <h4>
                      {isOpen
                        ? "High error rate detected"
                        : "Fault resolved"}
                    </h4>

                    <p>{event.serviceName}</p>

                    <span className="fault-time">
                      <Clock size={13} />
                      {event.errorCount} errors in{" "}
                      {event.windowSeconds} seconds
                    </span>

                    <p className="fault-time">
                      {formatDate(event.startedAt)}
                    </p>
                  </div>

                  <span
                    className={
                      isOpen
                        ? "fault-status"
                        : "resolved-status"
                    }
                  >
                    {event.status}
                  </span>
                </div>
              );
            })
          )}
        </div>

        <div className="dashboard-panel service-panel">
          <div className="panel-header">
            <h3>Service Health</h3>
            <Server size={20} />
          </div>

          {loading ? (
            <p>Loading services...</p>
          ) : services.length === 0 ? (
            <p>No services registered.</p>
          ) : (
            services.map((service) => (
              <div className="service-item" key={service.id}>
                <div>
                  <h4>{service.name}</h4>
                  <p>{service.environment}</p>
                </div>

                <span className="service-status">
                  REGISTERED
                </span>
              </div>
            ))
          )}
        </div>
      </section>
    </>
  );

  const renderServices = () => (
    <div className="dashboard-panel">
      <div className="panel-header">
        <h3>Registered Services</h3>
        <Server size={20} />
      </div>

      {loading ? (
        <p>Loading services...</p>
      ) : services.length === 0 ? (
        <p>No services registered yet.</p>
      ) : (
        services.map((service) => (
          <div className="service-item" key={service.id}>
            <div>
              <h4>{service.name}</h4>
              <p>{service.description || "No description"}</p>
              <p>Environment: {service.environment}</p>
            </div>

            <span className="service-status">REGISTERED</span>
          </div>
        ))
      )}
    </div>
  );

  const renderFaultEvents = () => (
    <div className="dashboard-panel">
      <div className="panel-header">
        <h3>All Fault Events</h3>
        <Activity size={20} />
      </div>

      {loading ? (
        <p>Loading fault events...</p>
      ) : faultEvents.length === 0 ? (
        <p>No fault events found.</p>
      ) : (
        faultEvents.map((event) => (
          <div className="fault-item" key={event.id}>
            <div className="fault-icon">
              <AlertTriangle size={20} />
            </div>

            <div>
              <h4>{event.serviceName}</h4>
              <p>{event.errorCount} errors detected</p>
              <p className="fault-time">
                {formatDate(event.startedAt)}
              </p>
            </div>

            <span
              className={
                event.status === "OPEN"
                  ? "fault-status"
                  : "resolved-status"
              }
            >
              {event.status}
            </span>
          </div>
        ))
      )}
    </div>
  );

  const renderLogs = () => (
    <div className="dashboard-panel">
      <div className="panel-header">
        <h3>Application Logs</h3>
        <FileText size={20} />
      </div>

      {logsLoading ? (
        <p>Loading logs...</p>
      ) : logsError ? (
        <p>{logsError}</p>
      ) : logs.length === 0 ? (
        <p>No logs found.</p>
      ) : (
        logs.map((log, index) => (
          <div className="fault-item" key={log.id ?? index}>
            <div className="fault-icon">
              <FileText size={20} />
            </div>

            <div>
              <h4>{log.level || "LOG"}</h4>
              <p>{log.message}</p>
              <p>{log.serviceName || log.service?.name || "Service"}</p>
              <p className="fault-time">
                {formatDate(log.timestamp || log.createdAt)}
              </p>
            </div>
          </div>
        ))
      )}
    </div>
  );

  const renderAlerts = () => (
    <div className="dashboard-panel">
      <div className="panel-header">
        <h3>Open Alerts</h3>
        <Bell size={20} />
      </div>

      {loading ? (
        <p>Loading alerts...</p>
      ) : openFaults.length === 0 ? (
        <p>No open alerts. All detected faults are resolved.</p>
      ) : (
        openFaults.map((event) => (
          <div className="fault-item" key={event.id}>
            <div className="fault-icon">
              <AlertTriangle size={20} />
            </div>

            <div>
              <h4>High error rate detected</h4>
              <p>{event.serviceName}</p>
              <p>{event.errorCount} errors in {event.windowSeconds} seconds</p>
              <p className="fault-time">
                {formatDate(event.startedAt)}
              </p>
            </div>

            <span className="fault-status">OPEN</span>
          </div>
        ))
      )}
    </div>
  );

  const renderSettings = () => (
    <div className="dashboard-panel">
      <div className="panel-header">
        <h3>Settings</h3>
      </div>
      <p>FaultLens application settings will be added in a future phase.</p>
      <p>Dashboard refresh interval: 15 seconds.</p>
    </div>
  );

  const renderPage = () => {
    switch (activePage) {
      case "Services":
        return renderServices();
      case "Fault Events":
        return renderFaultEvents();
      case "Logs":
        return renderLogs();
      case "Alerts":
        return renderAlerts();
      case "Settings":
        return renderSettings();
      default:
        return renderOverview();
    }
  };

  return (
    <div className="app-container">
      <Sidebar
        activePage={activePage}
        setActivePage={setActivePage}
      />

      <main className="main-content">
        <header className="top-header">
          <div>
            <h1>{activePage}</h1>
            <p>Monitor your application health and performance.</p>
          </div>

          <div className="system-status">
            <span className="status-dot"></span>
            System Monitoring
          </div>
        </header>

        {renderPage()}

        <footer className="dashboard-footer">
          FaultLens © 2026 | Application Monitoring Platform
        </footer>
      </main>
    </div>
  );
}

export default App;
